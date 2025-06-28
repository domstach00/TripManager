import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpenseDistributionChartComponent } from './expense-distribution-chart.component';

describe('ExpenseDistributionChartComponent', () => {
  let component: ExpenseDistributionChartComponent;
  let fixture: ComponentFixture<ExpenseDistributionChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpenseDistributionChartComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExpenseDistributionChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
