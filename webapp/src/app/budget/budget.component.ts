import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'budget',
    templateUrl: './budget.component.html',
    styleUrl: './budget.component.scss',
    standalone: false
})
export class BudgetComponent implements OnInit {
  budgetId!: string;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('budgetId');
      if (id) {
        this.budgetId = id;
      }
    });
  }
}
