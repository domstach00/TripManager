export class Trip {
  id: string = "";
  name: string = "";
  day_length: number = 0;
  summary_cost: number = 0;
  creation_date: Date | string = "";
  last_update_date: Date | string = "";
  created_by_user: string = "";
  planned_date?: Date | string;
  view_only_users?: string[];
  mod_users?: string[];
}
