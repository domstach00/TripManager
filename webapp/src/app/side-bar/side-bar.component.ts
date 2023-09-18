import { Component } from '@angular/core';

interface NavSideBarItem {
  name: string;
  ref: string;
  icon: string;
}

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss'],
})
export class SideBarComponent {
  list: NavSideBarItem[] = this.navItemList();


  navItemList(): NavSideBarItem[] {
    return [
      this.home(),
      this.settings(),
      this.user(),
      this.trips()
    ]
  }

  home() {
    return this.generateNavSideBarItem("Home","/home", "home");
  }

  settings() {
    return this.generateNavSideBarItem("Settings", "/settings", "settings");
  }

  user() {
    return this.generateNavSideBarItem("User", "/login", "edit")
  }

  trips() {
    return this.generateNavSideBarItem("My trips" , "/trips", "travel_explore")
  }

  private generateNavSideBarItem(name: string, ref: string, icon: string) {
    return {
      name: name,
      ref: ref,
      icon: icon
    } as NavSideBarItem
  }

}
