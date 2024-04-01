// https://www.google.com/maps/d/u/0/viewer?mid=1icXjgXJ5da1l2BQjMNgXAI4dlkw&hl=en_US&ll=-0.004507912916095585%2C0.006232240233883246&z=17
export const MAP_ICON_PATH = 'assets/map/icons/';

export enum MapIcon {
  RED_DOT = MAP_ICON_PATH + 'red-dot.png',
  BLUE_DOT = MAP_ICON_PATH + 'blue-dot.png',
  GREEN_DOT = MAP_ICON_PATH + 'green-dot.png',
  CYAN_DOT = MAP_ICON_PATH + 'ltblue-dot.png',
  YELLOW_DOT = MAP_ICON_PATH + 'yellow-dot.png',
  PURPLE_DOT = MAP_ICON_PATH + 'purple-dot.png',
  PINK_DOT = MAP_ICON_PATH + 'pink-dot.png',
  RESTAURANT = MAP_ICON_PATH + 'restaurant.png',
  CAFE = MAP_ICON_PATH + 'coffeehouse.png',
  BAR = MAP_ICON_PATH + 'bar.png',
  FAST_FOOD = MAP_ICON_PATH + 'snack_bar.png',
  BUS = MAP_ICON_PATH + 'bus.png',
  CABS = MAP_ICON_PATH + 'cabs.png',
  FERRY = MAP_ICON_PATH + 'ferry.png',
  HELICOPTER = MAP_ICON_PATH + 'helicopter.png',
  PARKING_LOT = MAP_ICON_PATH + 'parkinglot.png',
  PLANE = MAP_ICON_PATH + 'plane.png',
  RAIL = MAP_ICON_PATH + 'rail.png',
  SUBWAY = MAP_ICON_PATH + 'subway.png',
  TRAM = MAP_ICON_PATH + 'tram.png',
}

export function getIconPath(icon?: string): string | null {
  return MapIcon[icon as keyof typeof MapIcon] || null;
}
