/**
 * Created by coocloud on 4/1/2016.
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


public class CodeOff6 {

    public static void main(String[] args) throws InterruptedException {
        Path path = Paths.get("C:\\Projects\\Other\\src\\main\\java\\code_off-6-2.in");
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
        Maze2D maze = new Maze2D(myStringArray);
        final Point origin = maze.getInitialLoc();
        Point goal = maze.getGoalLoc();

        SearchProblem p = null;
        p = getSearchProblem(maze, origin);
        List<List<Point>> optimalPaths= null;
        List<List<Point>> optimalCoffeePath= null;
        int optimalPathCount = 10000;
        while(goal != null){
            //System.out.println(goal.x + ":" + goal.y);
            //MazeSearch.printSearch(Hipster.createAStar(p).iterator(), maze);
            optimalPaths= Hipster.createAStar(p).search(goal).getOptimalPaths();
            //System.out.println(optimalPaths.size());
            if(optimalPaths.get(0).size()<optimalPathCount){
                optimalPathCount = optimalPaths.get(0).size();
                optimalCoffeePath = optimalPaths;
            }
            for (List<Point> optimalPath : optimalPaths) {
                //System.out.println(optimalPath.size());
                for (Point point : optimalPath) {
                    if(maze.getMaze()[point.y][point.x] != 'S' && maze.getMaze()[point.y][point.x] != 'G'){
                        //maze.getMaze()[point.y][point.x] = '.';
                    }
                    if(maze.getMaze()[point.y][point.x] == 'S'){
                        //maze.getMaze()[point.y][point.x] = '@';
                    }
                    if(maze.getMaze()[point.y][point.x] == 'G'){
                        maze.getMaze()[point.y][point.x] = 'U';
                    }
                }
            }
            //System.out.println(maze);
            //maze.getMaze()[goal.y][goal.x] = '#';
            goal =charToPoint('G',maze);
            //System.out.println(goal.x + ":" + goal.y);
        }
//        p = getSearchProblem(maze, origin);
        //optimalPaths = Hipster.createAStar(p).search(goal).getOptimalPaths();
        //System.out.println(optimalCoffeePath.size());
        //System.out.println(optimalCoffeePath.get(0).size());
        for (List<Point> optimalPath : optimalCoffeePath) {
            for (Point point : optimalPath) {
                if(maze.getMaze()[point.y][point.x] != 'S' && maze.getMaze()[point.y][point.x] != 'G' && maze.getMaze()[point.y][point.x] != 'U'){
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

    private static SearchProblem getSearchProblem(final Maze2D maze, Point origin) {
        return ProblemBuilder.create()
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
                    .build();
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
    public static Point charToPoint(char c, Maze2D maze) {
        for(int row = 0; row < maze.getMaze().length; row++) {
            for(int column = 0; column < maze.getMaze()[0].length; column++) {
                if(maze.getMaze()[row][column] == c) {
                    return new Point(column, row);
                }
            }
        }

        return null;
    }
}

