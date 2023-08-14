package com.celled.plugins.googlefit;


import static com.celled.plugins.googlefit.Constant.*;
import static com.celled.plugins.googlefit.Constant.KEY.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(
  name = "Fitness",
  requestCodes = {
    FitnessPlugin.REQUEST_CODE_GOOGLE_FIT_PERMISSIONS,
    FitnessPlugin.REQUEST_CODE_GOOGLE_SIGN_IN}
)
@SuppressLint("SimpleDateFormat")
@SuppressWarnings({"deprecation", "unused"})
public class FitnessPlugin extends Plugin {

  public static final String TAG = "HistoryApi";
  static final int REQUEST_CODE_GOOGLE_FIT_PERMISSIONS = 19849;
  static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1337;

  private FitnessOptions getFitnessSignInOptions() {
    // FitnessOptions instance, declaring the Fit API data types
    // and access required
    return FitnessOptions
      .builder()
      .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
      .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
      .build();
  }

  private GoogleSignInAccount getAccount() {
    return GoogleSignIn.getLastSignedInAccount(getActivity());
  }

  private void requestPermissions() {
    GoogleSignIn.requestPermissions(getActivity(), REQUEST_CODE_GOOGLE_FIT_PERMISSIONS, getAccount(), getFitnessSignInOptions());
  }


  @PluginMethod
  public void connectToGoogleFit(PluginCall pluginCall) {
    GoogleSignInAccount account = getAccount();
    if (account == null) {
      GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build();
      GoogleSignInClient signInClient = GoogleSignIn.getClient(this.getActivity(), googleSignInOptions);

      Intent intent = signInClient.getSignInIntent();
      startActivityForResult(pluginCall, intent, REQUEST_CODE_GOOGLE_SIGN_IN);
    } else {
      requestPermissions();
    }
    pluginCall.resolve();
  }

  @PluginMethod
  public void hasAccessToGoogleFit(PluginCall pluginCall) {
    GoogleSignInAccount account = getAccount();
    final boolean accessibility = account != null && GoogleSignIn.hasPermissions(account, getFitnessSignInOptions());

    final JSObject result = new JSObject();
    result.put(Constant.KEY.HAS_ACCESS, accessibility);
    pluginCall.resolve(result);
  }




  @Override
  protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
    super.handleOnActivityResult(requestCode, resultCode, data);
    PluginCall savedCall = getSavedCall();
    if(savedCall == null) return;

    if (requestCode == REQUEST_CODE_GOOGLE_FIT_PERMISSIONS) {
      savedCall.resolve();
    } else if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
      if (!GoogleSignIn.hasPermissions(getAccount(), getFitnessSignInOptions())) {
        requestPermissions();
      } else {
        savedCall.resolve();
      }
    }
  }

  @PluginMethod
  public Task<DataReadResponse> getSteps(final PluginCall pluginCall) throws ParseException {
    final GoogleSignInAccount account = getAccount();

    if (account == null) {
      pluginCall.reject("No access");
      return null;
    }

    long startTime = dateToTimestamp(pluginCall.getString("startTime"));
    long endTime = dateToTimestamp(pluginCall.getString("endTime"));

    String timeUnitInput = pluginCall.getString("timeUnit", "HOURS");

    TimeUnit timeUnit = stringToTimeUnit(timeUnitInput);

    int bucketSize = pluginCall.getInt("bucketSize", 1);

    if (startTime == -1 || endTime == -1 || bucketSize == -1) {
      pluginCall.reject("Must provide a start time and end time");

      return null;
    }

    DataReadRequest readRequest = new DataReadRequest.Builder()
      .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
      .setTimeRange(startTime, endTime, TimeUnit.SECONDS)
      .bucketByTime(bucketSize, timeUnit)
      .enableServerQueries()
      .build();

    return Fitness
      .getHistoryClient(getActivity(), account)
      .readData(readRequest)
      .addOnSuccessListener(
        new OnSuccessListener<DataReadResponse>() {
          @Override
          public void onSuccess(DataReadResponse dataReadResponse) {
            List<Bucket> buckets = dataReadResponse.getBuckets();

            JSONArray steps = new JSONArray();

            for (Bucket bucket : buckets) {
              for (DataSet dataSet : bucket.getDataSets()) {
                for (DataPoint dp : dataSet.getDataPoints()) {
                  for (Field field : dp.getDataType().getFields()) {
                    JSONObject stepEntry = new JSONObject();
                    try {
                      stepEntry.put("startTime", timestampToDate(dp.getStartTime(TimeUnit.MILLISECONDS)));
                      stepEntry.put("endTime", timestampToDate(dp.getEndTime(TimeUnit.MILLISECONDS)));
                      stepEntry.put("value", dp.getValue(field).asInt());

                      final long activeTime = dp.getEndTime(TimeUnit.SECONDS) - dp.getStartTime(TimeUnit.SECONDS);
                      Log.i(TAG, "Active time " + activeTime);
                      stepEntry.put("duration", activeTime+"");
                      steps.put(stepEntry);
                    } catch (JSONException e) {
                      pluginCall.reject(e.getMessage());
                      return;
                    }
                  }
                }
              }
            }

            JSObject result = new JSObject();
            result.put("steps", steps);

            pluginCall.resolve(result);
          }
        }
      );
  }


  /**
   *
   * @param call
   * @return
   * @throws ParseException - Signals that an error has been reached unexpectedly while parsing
   */
  @PluginMethod
  public Task<DataReadResponse> getWeight(final PluginCall call) throws ParseException {
    final GoogleSignInAccount account = getAccount();

    if (account == null) {
      call.reject("No access");
      return null;
    }

    long startTime = dateToTimestamp(call.getString("startTime"));
    long endTime = dateToTimestamp(call.getString("endTime"));

    if (startTime == -1 || endTime == -1) {
      call.reject("Must provide a start time and end time");

      return null;
    }
    DataReadRequest readRequest = new DataReadRequest.Builder()
      .read(DataType.TYPE_WEIGHT)
      .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
      .enableServerQueries()
      .build();

    return Fitness
      .getHistoryClient(getActivity(), account)
      .readData(readRequest)
      .addOnSuccessListener(
        new OnSuccessListener<DataReadResponse>() {
          @Override
          public void onSuccess(DataReadResponse dataReadResponse) {
            DataSet weightDataSet = dataReadResponse.getDataSet(DataType.TYPE_WEIGHT);

            JSONArray weights = new JSONArray();
            for (DataPoint dp : weightDataSet.getDataPoints()) {
              for (Field field : dp.getDataType().getFields()) {
                JSONObject weightEntry = new JSONObject();
                try {
                  weightEntry.put("startTime", timestampToDate(dp.getStartTime(TimeUnit.MILLISECONDS)));
                  weightEntry.put("endTime", timestampToDate(dp.getEndTime(TimeUnit.MILLISECONDS)));
                  weightEntry.put("value", dp.getValue(field).asFloat());
                  weights.put(weightEntry);
                } catch (JSONException e) {
                  call.reject(e.getMessage());
                  return;
                }
              }
            }
            JSObject result = new JSObject();
            result.put("weights", weights);
            call.resolve(result);
          }
        }
      );
  }


  /**
   * isGoogleFitAppInstalled
   * This function will indicate if the Google Fit app installed or not in the device
   * @param  pluginCall {PluginCall}- Wraps a call from the web layer to native
   * return -> hasAccess {boolean} - true: if app is already installed | false: app is not installed
   */
  @PluginMethod
  public void isGoogleFitAppInstalled(final PluginCall pluginCall){
    final JSObject result = new JSObject();

    try {
      PackageManager packageManager = getContext().getPackageManager();
      packageManager.getPackageInfo(Constant.GOOGLE_FIT_PACKAGE_NAME, PackageManager.GET_META_DATA);

      result.put(Constant.KEY.HAS_ACCESS, true);
    } catch (PackageManager.NameNotFoundException e) {
      result.put(Constant.KEY.HAS_ACCESS, false);
    }
    pluginCall.resolve(result);
  }


  @PluginMethod
  public void getConnectedGoogleAccountData(final PluginCall pluginCall){
    GoogleSignInAccount account = getAccount();

    if (account == null) {
      pluginCall.reject(ERROR_MSG_ACCOUNT_NOT_FOUND);
      return;
    }

    final JSObject result = new JSObject();
    result.put(DISPLAY_NAME, account.getDisplayName());
    result.put(EMAIL, account.getEmail());
    pluginCall.resolve(result);
  }


  @PluginMethod
  public void requestPermissionForNewConnection(final PluginCall pluginCall) throws ParseException {
    GoogleSignInAccount account = getAccount();

    if (account == null) {
      connectToGoogleFit(pluginCall);
      return;
    }

    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    final GoogleSignInClient signInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

    signInClient.signOut()
            .addOnCompleteListener(this.getActivity(), task -> {
              Log.i(TAG, ">> signOut complete");

              signInClient.revokeAccess().addOnCompleteListener(getActivity(), task1 -> {
                Log.i(TAG, ">> revokeAccess complete");

                Intent intent = GoogleSignIn.getClient(getContext(),
                                googleSignInOptions)
                        .getSignInIntent();
                startActivityForResult(pluginCall, intent, REQUEST_CODE_GOOGLE_SIGN_IN);
              });
            });
  }




  @PluginMethod
  public Task<DataReadResponse> getActivities(final PluginCall call) throws ParseException {
    final GoogleSignInAccount account = getAccount();

    if (account == null) {
      call.reject("No access");
      return null;
    }

    long startTime = dateToTimestamp(call.getString("startTime"));
    long endTime = dateToTimestamp(call.getString("endTime"));
    String timeUnitInput = call.getString("timeUnit", "HOURS");

    TimeUnit timeUnit = stringToTimeUnit(timeUnitInput);

    int bucketSize = call.getInt("bucketSize", 1);

    if (startTime == -1 || endTime == -1 || bucketSize == -1) {
      call.reject("Must provide a start time and end time");

      return null;
    }
    DataReadRequest readRequest = new DataReadRequest.Builder()
      .aggregate(DataType.TYPE_CALORIES_EXPENDED)
      .aggregate(DataType.AGGREGATE_CALORIES_EXPENDED)
      .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
      .bucketByActivitySegment(bucketSize, timeUnit)
      .enableServerQueries()
      .build();

    return Fitness
      .getHistoryClient(getActivity(), account)
      .readData(readRequest)
      .addOnSuccessListener(
        new OnSuccessListener<DataReadResponse>() {
          @Override
          public void onSuccess(DataReadResponse dataReadResponse) {
            List<Bucket> activityBuckets = dataReadResponse.getBuckets();

            JSONArray activities = new JSONArray();

            for (Bucket bucket : activityBuckets) {
              JSONObject activity = new JSONObject();
              try {
                activity.put("startTime", timestampToDate(bucket.getStartTime(TimeUnit.MILLISECONDS)));
                activity.put("endTime", timestampToDate(bucket.getEndTime(TimeUnit.MILLISECONDS)));
                List<DataSet> activityDataSets = bucket.getDataSets();

                for (DataSet ds : activityDataSets) {
                  dumpDataSet(ds);
                }

//                for (DataSet ds : activityDataSets) {
//                  for (DataPoint dp : ds.getDataPoints()) {
//                    for (Field field : dp.getDataType().getFields()) {
//                      String dataTypeName = dp.getDataType().getName();
//                      if ("com.google.calories.expended".equals(dataTypeName)) {
//                        activity.put("calories", dp.getValue(field).asFloat());
//                      }
//                    }
//                  }
//                }
                activity.put("name", bucket.getActivity());
                activities.put(activity);
              } catch (JSONException e) {
                call.reject(e.getMessage());
                return;
              }
            }
            JSObject result = new JSObject();
            result.put("activities", activities);

            call.resolve(result);
          }
        }
      );
  }

  private void dumpDataSet(DataSet dataSet) {
    Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
    for (DataPoint dp : dataSet.getDataPoints()) {
      Log.i(TAG, "Data point:");
      Log.i(TAG, "\tType: " + dp.getDataType().getName());
      Log.i(TAG, "\tStart: " + dp.getStartTime(TimeUnit.MILLISECONDS));
      Log.i(TAG, "\tEnd: " + dp.getEndTime(TimeUnit.MILLISECONDS));
      for (Field field : dp.getDataType().getFields()) {
        Log.i(TAG, "\tField: " + field.getName() + "   Value: " + dp.getValue(field));
      }
    }
  }

  private String timestampToDate(long timestamp) {

    SimpleDateFormat simpleDateFormat = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)?
                    new SimpleDateFormat(DATE_FORMAT_DEFAULT):
                    new SimpleDateFormat();

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timestamp);
    return simpleDateFormat.format(calendar.getTime());
  }

  private long dateToTimestamp(String date) {
    if (date == null || date.isEmpty()) return -1;

    SimpleDateFormat simpleDateFormat = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)?
                    new SimpleDateFormat(DATE_FORMAT_DEFAULT):
                    new SimpleDateFormat();
    try {
      Date dateObj = simpleDateFormat.parse(date);
      if(dateObj != null) return dateObj.getTime();

    } catch (ParseException parseException) {
      Log.e(TAG, parseException.getMessage());
    }
    return -1;
  }

  private TimeUnit stringToTimeUnit(String inputString) {
    switch (inputString) {
      case "NANOSECONDS":
        return TimeUnit.NANOSECONDS;
      case "MICROSECONDS":
        return TimeUnit.MICROSECONDS;
      case "MILLISECONDS":
        return TimeUnit.MILLISECONDS;
      case "SECONDS":
        return TimeUnit.SECONDS;
      case "MINUTES":
        return TimeUnit.MINUTES;
      case "DAYS":
        return TimeUnit.DAYS;
      default:
        return TimeUnit.HOURS;
    }
  }
}
