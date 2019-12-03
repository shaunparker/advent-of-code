(ns com.shaunshaun.aoc2019.day01
  "--- Day 1: The Tyranny of the Rocket Equation ---
   Santa has become stranded at the edge of the Solar System while delivering
   presents to other planets! To accurately calculate his position in space,
   safely align his warp drive, and return to Earth in time to save Christmas,
   he needs you to bring him measurements from fifty stars.

   Collect stars by solving puzzles. Two puzzles will be made available on each
   day in the Advent calendar; the second puzzle is unlocked when you complete
   the first. Each puzzle grants one star. Good luck!

   The Elves quickly load you into a spacecraft and prepare to launch.

   At the first Go / No Go poll, every Elf is Go until the Fuel Counter-Upper.
   They haven't determined the amount of fuel required yet."
  (:require
    [clojure.spec-alpha2 :as s]))

(create-ns 'warp-drive)
(alias 'wd 'warp-drive)

(s/def ::wd/fuel nat-int?)

(s/def ::wd/mass nat-int?)

(s/def ::wd/masses (s/coll-of ::wd/mass))

(defn mass->fuel
  "The fuel required for a module, take its mass, divide by three,
   round down, and subtract 2.

   A mass of 12, divide by 3 and round down to get 4, then subtract 2 to get 2.
   A mass of 14, dividing by 3 and rounding down still yields 4, so the fuel
   required is also 2.
   A mass of 1969, the fuel required is 654.
   A mass of 100756, the fuel required is 33583."
  [mass]
  (-> (/ mass 3.0)
      (Math/floor)
      (- 2)
      (long)))

(defn mass->fuel-seq
  "Fuel itself requires fuel just like a module - take its mass, divide by
   three, round down, and subtract 2. However, that fuel also requires fuel,
   and that fuel requires fuel, and so on. Any mass that would require negative
   fuel should instead be treated as if it requires zero fuel; the remaining
   mass, if any, is instead handled by wishing really hard, which has no mass
   and is outside the scope of this calculation.

   So, for each module mass, calculate its fuel and add it to the total. Then,
   treat the fuel amount you just calculated as the input mass and repeat the
   process, continuing until a fuel requirement is zero or negative."
  [mass]
  (->> (iterate mass->fuel mass)
       ;; skip the first element, its the provided mass
       (rest)
       (take-while pos-int?)))

(defn fuel-counter-upper
  "The Fuel Counter-Upper needs to know the total fuel requirement. To find it,
   individually calculate the fuel needed for the mass of each module
   (your puzzle input), then add together all the fuel values."
  [masses]
  (->> (map mass->fuel masses)
       (reduce +)))

(defn masses->fuel
  "Calculates the fuel requirements for each mass separately and adds them all up."
  [masses]
  (->> (mapcat mass->fuel-seq masses)
       (reduce +)))

(comment
  (let [masses [145963 119152 122543 145710 133900 132606 52408 53565 59976
                81701 121675 107404 134873 141724 138465 96966 77092 127607
                142761 77766 68747 126829 54471 148637 69409 104756 144862 81599
                82815 123923 125193 60380 84496 98728 97193 111796 144980 135001
                136717 82743 78261 143756 127891 111665 121793 136152 144144
                117761 108060 94355 93797 123979 122509 114558 140655 94911
                94615 54266 149172 101186 132465 108057 134115 74910 63397
                132916 56643 142422 68900 146027 63015 71272 118759 101247
                114596 147249 92866 93567 84820 67882 87459 148556 71855 53522
                101978 82314 86692 102372 92084 99883 62642 57330 110474 70679
                101075 79706 79487 139548 122700 96657]
        _ (assert (s/valid? ::wd/masses masses))
        part1-answer (fuel-counter-upper masses)
        part2-answer (masses->fuel masses)]
    ;; What is the sum of the fuel requirements for all of the modules on
    ;; your spacecraft?
    (println "Day 01 Part 1 Answer:" part1-answer)
    ;; What is the sum of the fuel requirements for all of the modules on your
    ;; spacecraft when also taking into account the mass of the added fuel?
    (println "Day 01 Part 2 Answer:" part2-answer)))