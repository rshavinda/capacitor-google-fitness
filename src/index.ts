import { registerPlugin } from '@capacitor/core';

import type { FitnessPlugin } from './definitions';

const Fitness = registerPlugin<FitnessPlugin>('Fitness', {
  web: () => import('./web').then(m => new m.FitnessWeb()),
});



export * from './definitions';
export { Fitness };


