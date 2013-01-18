/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fi.oamk.ofkl.solver.move.factory;

import fi.oamk.ofkl.domain.Player;
import fi.oamk.ofkl.domain.TeamAssignment;
import fi.oamk.ofkl.domain.TeamSolution;
import fi.oamk.ofkl.solver.move.TeamAssignmentMove;
import org.drools.planner.core.heuristic.selector.move.factory.MoveListFactory;
import org.drools.planner.core.move.Move;
import org.drools.planner.core.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class TeamAssignmentChangeMoveFactory implements MoveListFactory {

    @Override
    public List<Move> createMoveList(Solution solution) {
        TeamSolution teamSolution = (TeamSolution) solution;
        List<Move> moveList = new ArrayList<Move>();
        List<Player> playerList = teamSolution.getPlayerList();
        for (TeamAssignment assignment : teamSolution.getTeamAssignments()) {
            for (Player player : playerList) {
                TeamAssignmentMove teamAssignmentMove = new TeamAssignmentMove(assignment, player);
                moveList.add(teamAssignmentMove);
            }
        }
        return moveList;
    }

}
