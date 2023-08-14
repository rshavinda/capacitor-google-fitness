# google-fitness

Capacitor plugin for google fit

## Install

```bash
npm install google-fitness
npx cap sync
```

## API

<docgen-index>

* [`connectToGoogleFit()`](#connecttogooglefit)
* [`hasAccessToGoogleFit()`](#hasaccesstogooglefit)
* [`getSteps(...)`](#getsteps)
* [`getWeight(...)`](#getweight)
* [`getActivities(...)`](#getactivities)
* [`getConnectedGoogleAccountData()`](#getconnectedgoogleaccountdata)
* [`isGoogleFitAppInstalled()`](#isgooglefitappinstalled)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### connectToGoogleFit()

```typescript
connectToGoogleFit() => Promise<void>
```

Connect to Google Fit

--------------------


### hasAccessToGoogleFit()

```typescript
hasAccessToGoogleFit() => Promise<AccessData>
```

Returns wether the permissions are ok or not

**Returns:** <code>Promise&lt;<a href="#accessdata">AccessData</a>&gt;</code>

--------------------


### getSteps(...)

```typescript
getSteps(call: ExtendedQueryInput) => Promise<StepQueryResult>
```

Get step history @param bucketSize and @param timeUnit you can define the buckets in which the data is returned

| Param      | Type                                                              |
| ---------- | ----------------------------------------------------------------- |
| **`call`** | <code><a href="#extendedqueryinput">ExtendedQueryInput</a></code> |

**Returns:** <code>Promise&lt;<a href="#stepqueryresult">StepQueryResult</a>&gt;</code>

--------------------


### getWeight(...)

```typescript
getWeight(call: QueryInput) => Promise<WeightQueryResult>
```

Get weight history

| Param      | Type                                              |
| ---------- | ------------------------------------------------- |
| **`call`** | <code><a href="#queryinput">QueryInput</a></code> |

**Returns:** <code>Promise&lt;<a href="#weightqueryresult">WeightQueryResult</a>&gt;</code>

--------------------


### getActivities(...)

```typescript
getActivities(call: ExtendedQueryInput) => Promise<ActivityQueryResult>
```

Get Activites, with @param bucketSize and @param timeUnit you can define the minimum length of an activity

| Param      | Type                                                              |
| ---------- | ----------------------------------------------------------------- |
| **`call`** | <code><a href="#extendedqueryinput">ExtendedQueryInput</a></code> |

**Returns:** <code>Promise&lt;<a href="#activityqueryresult">ActivityQueryResult</a>&gt;</code>

--------------------


### getConnectedGoogleAccountData()

```typescript
getConnectedGoogleAccountData() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### isGoogleFitAppInstalled()

```typescript
isGoogleFitAppInstalled() => Promise<AccessData>
```

**Returns:** <code>Promise&lt;<a href="#accessdata">AccessData</a>&gt;</code>

--------------------


### Interfaces


#### AccessData

| Prop            | Type                 |
| --------------- | -------------------- |
| **`hasAccess`** | <code>boolean</code> |


#### StepQueryResult

The results of a StepQuery.
The @param value inside of <a href="#simpledata">SimpleData</a> always represents a count

| Prop        | Type                      |
| ----------- | ------------------------- |
| **`steps`** | <code>SimpleData[]</code> |


#### SimpleData

| Prop            | Type                |
| --------------- | ------------------- |
| **`startTime`** | <code>string</code> |
| **`endTime`**   | <code>string</code> |
| **`value`**     | <code>number</code> |


#### ExtendedQueryInput

| Prop             | Type                |
| ---------------- | ------------------- |
| **`bucketSize`** | <code>number</code> |
| **`timeUnit`**   | <code>any</code>    |


#### WeightQueryResult

The results of a WeightQuery.
The @param value inside of <a href="#simpledata">SimpleData</a> has the unit kilograms

| Prop          | Type                      |
| ------------- | ------------------------- |
| **`weights`** | <code>SimpleData[]</code> |


#### QueryInput

| Prop            | Type                                  |
| --------------- | ------------------------------------- |
| **`startTime`** | <code><a href="#date">Date</a></code> |
| **`endTime`**   | <code><a href="#date">Date</a></code> |


#### Date

Enables basic storage and retrieval of dates and times.

| Method                 | Signature                                                                                                    | Description                                                                                                                             |
| ---------------------- | ------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------- |
| **toString**           | () =&gt; string                                                                                              | Returns a string representation of a date. The format of the string depends on the locale.                                              |
| **toDateString**       | () =&gt; string                                                                                              | Returns a date as a string value.                                                                                                       |
| **toTimeString**       | () =&gt; string                                                                                              | Returns a time as a string value.                                                                                                       |
| **toLocaleString**     | () =&gt; string                                                                                              | Returns a value as a string value appropriate to the host environment's current locale.                                                 |
| **toLocaleDateString** | () =&gt; string                                                                                              | Returns a date as a string value appropriate to the host environment's current locale.                                                  |
| **toLocaleTimeString** | () =&gt; string                                                                                              | Returns a time as a string value appropriate to the host environment's current locale.                                                  |
| **valueOf**            | () =&gt; number                                                                                              | Returns the stored time value in milliseconds since midnight, January 1, 1970 UTC.                                                      |
| **getTime**            | () =&gt; number                                                                                              | Gets the time value in milliseconds.                                                                                                    |
| **getFullYear**        | () =&gt; number                                                                                              | Gets the year, using local time.                                                                                                        |
| **getUTCFullYear**     | () =&gt; number                                                                                              | Gets the year using Universal Coordinated Time (UTC).                                                                                   |
| **getMonth**           | () =&gt; number                                                                                              | Gets the month, using local time.                                                                                                       |
| **getUTCMonth**        | () =&gt; number                                                                                              | Gets the month of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                             |
| **getDate**            | () =&gt; number                                                                                              | Gets the day-of-the-month, using local time.                                                                                            |
| **getUTCDate**         | () =&gt; number                                                                                              | Gets the day-of-the-month, using Universal Coordinated Time (UTC).                                                                      |
| **getDay**             | () =&gt; number                                                                                              | Gets the day of the week, using local time.                                                                                             |
| **getUTCDay**          | () =&gt; number                                                                                              | Gets the day of the week using Universal Coordinated Time (UTC).                                                                        |
| **getHours**           | () =&gt; number                                                                                              | Gets the hours in a date, using local time.                                                                                             |
| **getUTCHours**        | () =&gt; number                                                                                              | Gets the hours value in a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                       |
| **getMinutes**         | () =&gt; number                                                                                              | Gets the minutes of a <a href="#date">Date</a> object, using local time.                                                                |
| **getUTCMinutes**      | () =&gt; number                                                                                              | Gets the minutes of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                           |
| **getSeconds**         | () =&gt; number                                                                                              | Gets the seconds of a <a href="#date">Date</a> object, using local time.                                                                |
| **getUTCSeconds**      | () =&gt; number                                                                                              | Gets the seconds of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                           |
| **getMilliseconds**    | () =&gt; number                                                                                              | Gets the milliseconds of a <a href="#date">Date</a>, using local time.                                                                  |
| **getUTCMilliseconds** | () =&gt; number                                                                                              | Gets the milliseconds of a <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                      |
| **getTimezoneOffset**  | () =&gt; number                                                                                              | Gets the difference in minutes between the time on the local computer and Universal Coordinated Time (UTC).                             |
| **setTime**            | (time: number) =&gt; number                                                                                  | Sets the date and time value in the <a href="#date">Date</a> object.                                                                    |
| **setMilliseconds**    | (ms: number) =&gt; number                                                                                    | Sets the milliseconds value in the <a href="#date">Date</a> object using local time.                                                    |
| **setUTCMilliseconds** | (ms: number) =&gt; number                                                                                    | Sets the milliseconds value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                              |
| **setSeconds**         | (sec: number, ms?: number \| undefined) =&gt; number                                                         | Sets the seconds value in the <a href="#date">Date</a> object using local time.                                                         |
| **setUTCSeconds**      | (sec: number, ms?: number \| undefined) =&gt; number                                                         | Sets the seconds value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                   |
| **setMinutes**         | (min: number, sec?: number \| undefined, ms?: number \| undefined) =&gt; number                              | Sets the minutes value in the <a href="#date">Date</a> object using local time.                                                         |
| **setUTCMinutes**      | (min: number, sec?: number \| undefined, ms?: number \| undefined) =&gt; number                              | Sets the minutes value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                   |
| **setHours**           | (hours: number, min?: number \| undefined, sec?: number \| undefined, ms?: number \| undefined) =&gt; number | Sets the hour value in the <a href="#date">Date</a> object using local time.                                                            |
| **setUTCHours**        | (hours: number, min?: number \| undefined, sec?: number \| undefined, ms?: number \| undefined) =&gt; number | Sets the hours value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                     |
| **setDate**            | (date: number) =&gt; number                                                                                  | Sets the numeric day-of-the-month value of the <a href="#date">Date</a> object using local time.                                        |
| **setUTCDate**         | (date: number) =&gt; number                                                                                  | Sets the numeric day of the month in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                        |
| **setMonth**           | (month: number, date?: number \| undefined) =&gt; number                                                     | Sets the month value in the <a href="#date">Date</a> object using local time.                                                           |
| **setUTCMonth**        | (month: number, date?: number \| undefined) =&gt; number                                                     | Sets the month value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                     |
| **setFullYear**        | (year: number, month?: number \| undefined, date?: number \| undefined) =&gt; number                         | Sets the year of the <a href="#date">Date</a> object using local time.                                                                  |
| **setUTCFullYear**     | (year: number, month?: number \| undefined, date?: number \| undefined) =&gt; number                         | Sets the year value in the <a href="#date">Date</a> object using Universal Coordinated Time (UTC).                                      |
| **toUTCString**        | () =&gt; string                                                                                              | Returns a date converted to a string using Universal Coordinated Time (UTC).                                                            |
| **toISOString**        | () =&gt; string                                                                                              | Returns a date as a string value in ISO format.                                                                                         |
| **toJSON**             | (key?: any) =&gt; string                                                                                     | Used by the JSON.stringify method to enable the transformation of an object's data for JavaScript Object Notation (JSON) serialization. |


#### ActivityQueryResult

The results of a ActivityQuery.
The @param value inside of <a href="#simpledata">SimpleData</a> has the values representing the Google Fit Constants as the name of the activity

| Prop             | Type                        |
| ---------------- | --------------------------- |
| **`activities`** | <code>ActivityData[]</code> |


#### ActivityData

| Prop           | Type                |
| -------------- | ------------------- |
| **`calories`** | <code>number</code> |
| **`name`**     | <code>string</code> |

</docgen-api>
