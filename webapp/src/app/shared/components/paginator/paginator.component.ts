import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from "@angular/material/tooltip";
import { TranslateModule } from "@ngx-translate/core";

@Component({
	selector: 'app-paginator',
	templateUrl: './paginator.component.html',
	styleUrl: './paginator.component.scss',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    TranslateModule
  ]
})
export class PaginatorComponent {
	readonly pageSortFun = (a: number, b: number) => a - b;
	private readonly _pageLinksToShow: number = 10;
	@Input() page: number;
	@Input() pageSize: number;
	@Input() totalElements: number;
	@Input() totalPages: number;
	@Input() isFirstPage: boolean;
	@Input() isLastPage: boolean;
	@Input() isEmptyPage: boolean;
	@Input() loading: boolean;

	@Output() goPrev = new EventEmitter<boolean>();
	@Output() goNext = new EventEmitter<boolean>();
	@Output() goPage = new EventEmitter<number>();

	shouldDisplayPaginator(): boolean {
		return this.totalPages > 1 && this.totalElements > 0
	}

	get isOnFirstFive(): boolean {
		return this.page <= 4
	}

	get isOnLastFive(): boolean {
		return ((this.totalPages - 1) - this.page) <= 5;
	}

	get pageLinksToShow(): number {
		return Math.min(this._pageLinksToShow, this.totalPages);
	}

	onNext(): void {
		this.page += 1;
		this.goNext.emit(true);
	}

	onPrev(): void {
		this.page -= 1;
		this.goPrev.emit(true);
	}

	onPage(n: number): void {
		this.page = n;
		this.goPage.emit(n);
	}

	getPages(): number[] {
		const c: number = this.totalPages;
		const p: number = this.page + 1;
		const pageLinksToShow: number = this.pageLinksToShow;
		const pages: number[] = [];
		pages.push(p);

		const times: number = pageLinksToShow - 1;
		for (let i = 0; i < times; i++) {
			if (pages.length < pageLinksToShow) {
				if (Math.min.apply(null, pages) > 1) {
					pages.push(
						Math.min.apply(null, pages) - 1
					);
				}
				if (pages.length < pageLinksToShow) {
					if (Math.max.apply(null, pages) < c) {
						pages.push(Math.max.apply(null, pages) + 1);
					}
				}
			}
		}

		if (!this.isOnFirstFive && !this.isOnLastFive && this.totalPages > pageLinksToShow) {
			const tempPages = [];
			const firstPages = [];
			const lastPages = [];
			const currentPage = this.page + 1;
			const nextPage = currentPage + 1;
			const firstPage = currentPage - 4;
			const lastPage = currentPage + 5;

			for (let i = firstPage; i <= currentPage; i++) {
				firstPages.push(i);
			}

			for (let i = nextPage; i <= lastPage; i++) {
				lastPages.push(i);
			}

			tempPages.push(...firstPages);
			tempPages.push(...lastPages);
			tempPages.sort(this.pageSortFun);
			return tempPages;
		}

		if (this.isOnLastFive && this.totalPages > pageLinksToShow) {
			const tempPages = [];
			for (let i = (this.totalPages - pageLinksToShow) + 1; i <= this.totalPages; i++) {
				tempPages.push(i);
			}
			tempPages.sort(this.pageSortFun);
			return tempPages;
		}

		pages.sort(this.pageSortFun);
		return pages;
	}
}
