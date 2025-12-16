package a06.e1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ZipCombinerFactoryImpl implements ZipCombinerFactory{

    @Override
    public <X, Y> ZipCombiner<X, Y, Pair<X, Y>> classical() {
        return new ZipCombiner<X,Y,Pair<X,Y>>() {
            @Override
            public List<Pair<X, Y>> zipCombine(List<X> l1, List<Y> l2) {
                return IntStream.range(0, Math.min(l1.size(), l2.size()))
                            .mapToObj(i -> new Pair<>(l1.get(i), l2.get(i)))
                            .toList();
            }  
        };
    }

    @Override
    public <X, Y, Z> ZipCombiner<X, Y, Z> mapFilter(Predicate<X> predicate, Function<Y, Z> mapper) {
        return new ZipCombiner<X,Y,Z>() {
            @Override
            public List<Z> zipCombine(List<X> l1, List<Y> l2) {
                return IntStream.range(0, l1.size())
                 .filter(i -> predicate.test(l1.get(i)))
                 .mapToObj(i -> mapper.apply(l2.get(i)))
                 .toList();
            }
        };
    }

    @Override
    public <Y> ZipCombiner<Integer, Y, List<Y>> taker() {
        return new ZipCombiner<Integer,Y,List<Y>>() {
            
            private final List<List<Y>> list = new LinkedList<>();

            @Override
            public List<List<Y>> zipCombine(List<Integer> l1, List<Y> l2) {
                int index = 0;
                for (Integer n : l1) {
                    final List<Y> nested = new ArrayList<>();
                    for(int i = 0; i < n && i < l2.size(); i++) {
                        nested.add(l2.get(index));
                        index++;
                    }           
                    list.add(nested);         
                }
                return list;
            }
        };
    }

    @Override
    public <X> ZipCombiner<X, Integer, Pair<X, Integer>> countUntilZero() {
        return new ZipCombiner<X,Integer,Pair<X,Integer>>() {
            
            private final List<Pair<X, Integer>> list = new LinkedList<>();
            
            @Override
            public List<Pair<X, Integer>> zipCombine(List<X> l1, List<Integer> l2) {
                int index = 0;
                for(X elem : l1) {
                    int count = 0;
                    while (index < l2.size() && l2.get(index) != 0) {
                        count++;
                        index++;
                    }
                    if (index < l2.size()) {
                        index++;
                    }
                    list.add(new Pair<>(elem, count));
                }
                return list;
            }
            
        };
    }

}
