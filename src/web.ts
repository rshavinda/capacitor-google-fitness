import { WebPlugin } from '@capacitor/core';

import type { AccessData, FitnessPlugin, StepQueryResult} from './definitions';

export class FitnessWeb extends WebPlugin implements FitnessPlugin {
  constructor() {
    super({
      name: 'Fitness',
      platforms: ['web'],
    });
  }

  async connectToGoogleFit(): Promise<void> {
    throw new Error('Method not implemented.');
  }
  async hasAccessToGoogleFit(): Promise<AccessData> {
    throw new Error('Method not implemented.');
  }
  async getSteps(): Promise<StepQueryResult> {
    throw new Error('Method not implemented.');
  }
  async getWeight(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  async getActivities(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  async getConnectedGoogleAccountData(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  async isGoogleFitAppInstalled(): Promise<AccessData> {
    throw new Error('Method not implemented.');
  }
}
