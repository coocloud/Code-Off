/**
 * Created by coocloud on 3/21/2016.
 */
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.maze.Maze2D;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class CodeOff5 {

    public static void main(String[] args) throws InterruptedException {
        Path path = Paths.get("C:\\Projects\\Other\\src\\main\\java\\code_off-5-2.in");
        List<String> myString = null;
        try {
            myString = Files.readAllLines(path, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i=0;
        String [] myStringArray = myString.toArray(new String[myString.size()]);
        //char[][] char2DArray = new char[myStringArray.length][myStringArray[0].length()];
        for (String s:myStringArray){
            //char2DArray[i] = s.replace('@','S').replace('U','G').toCharArray();
            myStringArray[i] = s.replace('@','S').replace('U','G');
            i++;
        }
        final Maze2D maze = new Maze2D(myStringArray);
        final Point origin = maze.getInitialLoc();
        final Point goal = maze.getGoalLoc();
        SearchProblem p = ProblemBuilder.create()
                .initialState(origin)
                .defineProblemWithoutActions()
                .useTransitionFunction(new StateTransitionFunction<Point>() {
                    @Override
                    public Iterable<Point> successorsOf(Point state) {
                        return validLocationsFrom(state, maze);
                    }
                })
                .useCostFunction(new CostFunction<Void, Point, Double>() {

                    @Override
                    public Double evaluate(Transition<Void, Point> transition) {
                        Point source = transition.getFromState();
                        Point destination = transition.getState();
                        return source.distance(destination);
                    }
                })
                .useHeuristicFunction(new HeuristicFunction<Point, Double>() {
                    @Override
                    public Double estimate(Point state) {
                        return state.distance(goal);
                    }
                })
                .build();
        //MazeSearch.printSearch(Hipster.createAStar(p).iterator(), maze);
        List<List<Point>> optimalPaths= Hipster.createAStar(p).search(goal).getOptimalPaths();
        //System.out.println(optimalPaths.size());
        for (List<Point> optimalPath : optimalPaths) {
            //System.out.println(optimalPath.size());
            for (Point point : optimalPath) {
                if(maze.getMaze()[point.y][point.x] != 'S' && maze.getMaze()[point.y][point.x] != 'G'){
                    maze.getMaze()[point.y][point.x] = '.';
                }
                if(maze.getMaze()[point.y][point.x] == 'S'){
                    maze.getMaze()[point.y][point.x] = '@';
                }
                if(maze.getMaze()[point.y][point.x] == 'G'){
                    maze.getMaze()[point.y][point.x] = 'U';
                }
            }
        }
        System.out.println(maze);
    }
    public static Collection<Point> validLocationsFrom(Point loc, Maze2D maze) {
        HashSet validMoves = new HashSet();
        for(int row = -1; row <= 1; ++row) {
            for(int column = -1; column <= 1; ++column) {
                try {
                    if(maze.isFree(new Point(loc.x, loc.y + row))) {
                        validMoves.add(new Point(loc.x, loc.y + row));
                    }
                    if(maze.isFree(new Point(loc.x + column, loc.y))) {
                        validMoves.add(new Point(loc.x + column, loc.y));
                    }
                } catch (ArrayIndexOutOfBoundsException var6) {
                }
            }
        }
        validMoves.remove(loc);
        return validMoves;
    }
}

