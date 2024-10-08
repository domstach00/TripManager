import { Injectable } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

@Injectable({
	providedIn: 'root'
})
export class DateUtilService {
	// Sample date formats
	// dd MM yyyy - 08 10 2024
	// dd/MM/yyyy - 08/10/2024
	// MM-dd-yyyy - 10-08-2024
	// yyyy.MM.dd - 2024.10.08
	// dd MMM yyyy - 08 oct 2024
	// MMM dd, yyyy - oct 08, 2024
	// yyyy-MM-dd - 2024-10-08
	// dd-MM-yy - 08-10-24
	// HH:mm:ss - 13:42:46
	// HH:mm - 13:42
	// dd/MM/yyyy HH:mm - 08/10/2024 13:42
	// yyyy-MM-dd HH:mm:ss - 2024-10-08 13:42:46
	// dd MMM yyyy, HH:mm - 08 oct 2024, 13:42
	// EEEE, dd MMMM yyyy - Tuesday, 08 October 2024
	// yyyyMMddTHHmmssZ - 20241008T134246Z

	dateFormat: string = 'dd/MM/yyyy HH:mm';
	dateFormatKey: string = 'date.format';
	dateFormatRegex: RegExp = this.getDateFormatRegex();

	constructor(
		protected readonly translate: TranslateService,
	) {
		const translatedDateFormat: string = translate.instant(this.dateFormatKey);
		if (!!translatedDateFormat && translatedDateFormat !== this.dateFormatKey) {
			this.dateFormat = translatedDateFormat;
			this.dateFormatRegex = this.getDateFormatRegex();
		}
	}

	getDateFormatRegex(): RegExp {
		return new RegExp(
			'^' +
			this.dateFormat
				.replace(/([^a-z])/gi, '\\$1')
				.replace(/[a-z]/gi, '\\d') +
			'$'
		);
	}

	formatDate(date: Date | string, format: string = this.dateFormat, locale?: string): string {
		date = new Date(date);
		const day: string = this.padZero(date.getDate());
		const month: string = this.padZero(date.getMonth() + 1);
		const year: string = date.getFullYear().toString();
		const shortYear: string = year.toString().slice(-2);
		const hours: string = this.padZero(date.getHours());
		const minutes: string = this.padZero(date.getMinutes());
		const seconds: string = this.padZero(date.getSeconds());
		const selectedLocale = locale ?? this.getLocale();
		const dayName: string = date.toLocaleDateString(selectedLocale, { weekday: 'long' });
		const monthName: string = date.toLocaleDateString(selectedLocale, { month: 'long' });
		const shortMonthName: string = date.toLocaleDateString(selectedLocale, { month: 'short' });

		return format
			.replace('dd', day)
			.replace('EEEE', dayName)
			.replace('MMMM', monthName)
			.replace('MMM', shortMonthName)
			.replace('MM', month)
			.replace('yyyy', year)
			.replace('yy', shortYear)
			.replace('HH', hours)
			.replace('mm', minutes)
			.replace('ss', seconds);
	}

	parseDate(dateStr: string, format: string = this.dateFormat): Date | null {
		const formatParts = format.match(/(dd|MM|yyyy|yy|HH|mm|ss|EEEE|MMMM|MMM)/g);
		const dateParts = dateStr.match(/\d+/g);

		if (!formatParts || !dateParts || formatParts.length !== dateParts.length) {
			return null;
		}

		let day: number | null = null;
		let month: number | null = null;
		let year: number | null = null;
		let hours: number | null = null;
		let minutes: number | null = null;
		let seconds: number | null = null;

		formatParts.forEach((part, index) => {
			const value = parseInt(dateParts[index], 10);
			switch (part) {
				case 'dd':
					day = value;
					break;
				case 'MM':
					month = value - 1; // Months indexes starts at 0
					break;
				case 'yyyy':
					year = value;
					break;
				case 'yy':
					year = 2000 + value; // Assume years range 2000 - 2099
					break;
				case 'HH':
					hours = value;
					break;
				case 'mm':
					minutes = value;
					break;
				case 'ss':
					seconds = value;
					break;
			}
		});

		if (day === null || month === null || year === null) {
			return null;
		}

		return new Date(year, month, day, hours ?? 0, minutes ?? 0, seconds ?? 0);
	}

	getLocale() {
		return new Intl.NumberFormat().resolvedOptions().locale
	}

	private padZero(value: number): string {
		return value < 10
			? '0' + value
			: value.toString();
	}
}
