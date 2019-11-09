package com.yakgwa.kullow.map;

import android.util.Log;

import androidx.annotation.NonNull;

import com.yakgwa.kullow.map.Path.Path;
import com.yakgwa.kullow.map.Point.Point;

import java.util.List;
import java.util.Stack;

/**
 *  Router is only one functional module.
 *  using routing.
 */
public class Router {

    private List<Point> allPoints;
    private List<Path> allPaths;
    private int pointNum;

    private boolean[][] graph;
    private Stack<Integer> routingStack;

    private Route route;

    public Router(@NonNull List<Point> allPoints,@NonNull List<Path> allPaths){
        this.allPoints = allPoints;
        this.allPaths = allPaths;

        pointNum = this.allPoints.size();
        Log.e("@Point NUM@",String.valueOf(pointNum));
    }


    public Route getRoute(@NonNull Point src,@NonNull Point dest) {
        route = new Route(src);
        routingStack = new Stack<>();

        // 각 Point 연결 그래프
        graph = new boolean[pointNum+1][pointNum+1];
        for(Path path : allPaths){
            graph[(int)path.getP1Id()][(int)path.getP2Id()] = true;
            graph[(int)path.getP2Id()][(int)path.getP1Id()] = true;
        }

        // visit
        routingStack.add((int)src.getPid());
        visit((int)src.getPid(), (int)dest.getPid());

        return route;
    }

    private void visit(int visitNode, int targetNode){
        if(visitNode == targetNode){
            // _TODO: make route from Stack, if it has min distance, save it._
            Stack<Integer> findRoute = (Stack<Integer>) routingStack.clone();
            Route newRoute = new Route(allPoints.get(findRoute.pop()-1));

            while(!findRoute.empty()){

                int pid = findRoute.pop();
//                Log.d("ROUTE_STACK", String.valueOf(pid));
                newRoute.addPoint(allPoints.get(pid-1));
            }
//            Log.e("ROUTE", route.getDistanceSrcToDest() + " , " + newRoute.getDistanceSrcToDest());
            if(route.getDistanceSrcToDest() == 0 || route.getDistanceSrcToDest() > newRoute.getDistanceSrcToDest()){
                route = newRoute;
            }

            return;
        }

        // Was not visited
        if(!graph[visitNode][0]){
            // Visit
            graph[visitNode][0] = true;

            // Find Neighbor Point
            for(int i = 1; i <= pointNum; i++){
                if(graph[visitNode][i] || graph[i][visitNode]){
//                    Log.i("NEIGHBOR_NODE", String.valueOf(i));

                    register(i, targetNode);
                }
            }
            graph[visitNode][0] = false;
        }

    }
    private void register(int neighborNode, int targetNode){
        routingStack.add(neighborNode);
        visit(neighborNode, targetNode);
        routingStack.pop();
    }
}
