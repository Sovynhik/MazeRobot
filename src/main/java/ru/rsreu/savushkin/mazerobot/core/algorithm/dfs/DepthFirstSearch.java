package ru.rsreu.savushkin.mazerobot.core.algorithm.dfs;

import ru.rsreu.savushkin.mazerobot.core.algorithm.PathFindingAlgorithm;
import ru.rsreu.savushkin.mazerobot.core.state.Environment;
import ru.rsreu.savushkin.mazerobot.core.state.State;

import java.util.*;

public class DepthFirstSearch implements PathFindingAlgorithm {

    @Override
    public <S extends State> List<S> findPath(Environment<S, ?> env) {
        S start = env.getInitialState();
        S goal = env.getGoalState();

        Set<S> visited = new HashSet<>();
        Deque<S> stack = new ArrayDeque<>();
        Map<S, S> parent = new HashMap<>();

        stack.push(start);
        visited.add(start);
        parent.put(start, null);

        while (!stack.isEmpty()) {
            S current = stack.pop();
            if (env.isGoal(current)) {
                return reconstructPath(parent, current);
            }

            for (var action : env.getPossibleActions(current)) {
                S next = env.applyAction(current, action);
                if (env.isValid(next) && !visited.contains(next)) {
                    stack.push(next);
                    visited.add(next);
                    parent.put(next, current);
                }
            }
        }
        return Collections.emptyList();
    }

    private <S extends State> List<S> reconstructPath(Map<S, S> parent, S end) {
        List<S> path = new ArrayList<>();
        S current = end;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    @Override
    public String getName() {
        return "Поиск в глубину (DFS)";
    }
}