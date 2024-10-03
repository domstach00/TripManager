import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss'
})
export class PaginatorComponent {
	readonly pageSortFun = (a: number, b: number) => a - b;
	readonly pageLinksToShow: number = 10;

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

	onNext(): void {
		this.goNext.emit(true);
	}

	onPrev(): void {
		this.goPrev.emit(true);
	}

	onPage(n: number): void {
		this.goPage.emit(n);
	}

	getPages(): number[] {
		const c: number = this.totalPages;
		const p: number = this.pageSize || 1;
		const pageLinksToShow: number = this.pageLinksToShow;
		const pages: number[] = [];
		pages.push(p);

		const times: number = pageLinksToShow - 1;
		for (let i = 0 ; i < times; i++) {
			if (pages.length < pageLinksToShow) {
				if (Math.min.apply(null, pages) > 1) {
					pages.push(
						Math.min.apply(null, pages) - 1
					);
				}
				if (pages.length < pageLinksToShow) {
					if (Math.min.apply(null, pages) < c) {
						pages.push(Math.max.apply(null, pages) + 1);
					}
				}
			}
		}

		if (!this.isOnFirstFive && !this.isOnLastFive && this.totalPages > this.pageLinksToShow) {
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

		if (this.isOnLastFive && this.totalPages > this.pageLinksToShow) {
			const tempPages = [];
			for (let i = (this.totalElements - this.pageLinksToShow) + 1; i <= this.totalPages; i++) {
				tempPages.push(i);
			}
			tempPages.sort(this.pageSortFun);
			return tempPages;
		}

		pages.sort(this.pageSortFun);
		return pages;
	}

}
