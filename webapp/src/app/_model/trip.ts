import { User } from "./user";

export interface Trip {
  id: string;
  name: string;
  description?: string;
  dayLength: number;
  summaryCost: number;
  lastUpdateDate: number[];
  lastUpdateTime: number[];
  lastUpdateBy: User;
}
