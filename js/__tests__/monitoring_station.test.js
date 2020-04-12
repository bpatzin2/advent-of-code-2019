import {sum, mapToCoords} from '../src/monitoring_station';

test('adds 1 + 2 to equal 3', () => {
  expect(sum(1, 2)).toBe(3);
});

test('converts astroid map to coordinates', () => {
  const astroidMap = [
    ["#", "."],
    ["#", "#"]
  ];
  expect(mapToCoords(astroidMap)).toEqual([
    {x: 0, y: 0},
    {x: 0, y: 1},
    {x: 1, y: 1},
  ]);
});