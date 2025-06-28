import { Component, Input, OnInit } from '@angular/core';
import { BudgetService } from '../_service/budget.service';
import { CategoryWithStats } from '../_model/budget';

@Component({
  selector: 'app-expense-distribution-chart',
  templateUrl: './expense-distribution-chart.component.html',
  styleUrl: './expense-distribution-chart.component.scss'
})
export class ExpenseDistributionChartComponent implements OnInit {
  @Input() budgetId!: string;

  single: any[] = [];

  view: [number, number] = [700, 400];

  // options
  gradient: boolean = false;
  showLegend: boolean = false;
  showLabels: boolean = true;
  isDoughnut: boolean = false;

  colorScheme: any = { domain: [] };

  constructor(private budgetService: BudgetService) { }

  ngOnInit(): void {
	  this.generateChart();
  }

  generateChart() {
	  this.budgetService.getExpenseDistribution(this.budgetId).subscribe(data => {
		  this.single = [...data.map((item: CategoryWithStats) => ({
			  name: item.name,
			  value: item.totalSpentAmount
		  }))];
		  this.colorScheme.domain = [...data.map((item: CategoryWithStats) => item?.color || this.generateRandomColor())];
	  });
  }

  public refreshChart(): void {
	  console.log('Refreshing expense distribution chart...');
	  this.colorScheme = { domain: [] };
	  this.single = [];
	  this.generateChart();
  }

  generateRandomColor(): string {
	  const letters = '0123456789ABCDEF';
	  let color = '#';
	  for (let i = 0; i < 6; i++) {
		  color += letters[Math.floor(Math.random() * 16)];
	  }
	  return color;
  }
}
