export interface FitnessPlugin {
   /**
     * Connect to Google Fit
     * @returns {Promise}
     * @resolve any
     */
   connectToGoogleFit(): Promise<void>;
   /**
    * Returns wether the permissions are ok or not
    * @returns {Promise}
    * @resolve AllowedResult
    */
   hasAccessToGoogleFit(): Promise<AccessData>;

   /**
    * Get step history @param bucketSize and @param timeUnit you can define the buckets in which the data is returned
    * @returns {Promise}
    * @resolve StepQueryResult
    */
   getSteps(call: ExtendedQueryInput): Promise<StepQueryResult>;
   /**
    * Get weight history
    * @returns {Promise}
    * @resolve WeightQueryResult
    */
   getWeight(call: QueryInput): Promise<WeightQueryResult>;
   /**
    * Get Activites, with @param bucketSize and @param timeUnit you can define the minimum length of an activity
    * @returns {Promise}
    * @resolve ActivityQueryResult
    */
   getActivities(call: ExtendedQueryInput): Promise<ActivityQueryResult>;

   getConnectedGoogleAccountData(): Promise<any> ;
   
   isGoogleFitAppInstalled(): Promise<AccessData> ;
}
export interface PermissionData {
   allowed: boolean;
}
export interface QueryInput {
   startTime: Date;
   endTime: Date;
}
export interface ExtendedQueryInput extends QueryInput {
   bucketSize: number;
   timeUnit: any;
}
/**
* The results of a ActivityQuery.
* The @param value inside of SimpleData has the values representing the Google Fit Constants as the name of the activity
*/
export interface ActivityQueryResult {
   activities: ActivityData[];
}
/**
* The results of a WeightQuery.
* The @param value inside of SimpleData has the unit kilograms
*/
export interface WeightQueryResult {
   weights: SimpleData[];
}
/**
* The results of a StepQuery.
* The @param value inside of SimpleData always represents a count
*/
export interface StepQueryResult {
   steps: SimpleData[];
}
export interface SimpleData {
   startTime: string;
   endTime: string;
   value: number;
}
export interface ActivityData extends SimpleData {
   calories: number;
   name: string;
}
export interface AccessData {
   hasAccess: boolean;
}

export declare enum TimeUnit {
   NANOSECONDS = "NANOSECONDS",
   MICROSECONDS = "MICROSECONDS",
   MILLISECONDS = "MILLISECONDS",
   SECONDS = "SECONDS",
   MINUTES = "MINUTES",
   HOURS = "HOURS",
   DAYS = "DAYS"
}


const TUnits = {
   NANOSECONDS : "NANOSECONDS",
   MICROSECONDS : "MICROSECONDS",
   MILLISECONDS : "MILLISECONDS",
   SECONDS : "SECONDS",
   MINUTES : "MINUTES",
   HOURS : "HOURS",
   DAYS : "DAYS"
 } as const;
 
 // Convert object key in a type
 type DateTime = typeof TUnits[keyof typeof TUnits]
 
 declare function unit(unit: DateTime): void;