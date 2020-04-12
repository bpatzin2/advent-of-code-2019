// @flow
// for each astroid, determine how many astroids it can see, take the max
//
// for one astroid
//  check if it's blocked
//  if not blocked, count it
// 
// take one astroid 
//    compute it's vector from the given
//    reduce vector (6,2 becomes 3,1)
//    from the astoid, step towards the given, checking if somethings exists there
export function sum(a: number, b: number) {
  return a + b;
}

type Coord = {x: number, y: number}

export function mapToCoords(astroidMap: Array<Array<string>>): Array<Coord> {
  return astroidMap.flatMap((row: Array<string>, y: number) => {
    return row
      .map((cell, x) => cell === "#" ? {x, y} : null)
      .filter(Boolean)
  })
}
