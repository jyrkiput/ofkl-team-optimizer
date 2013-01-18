package fi.oamk.ofkl.solver.move;

import fi.oamk.ofkl.domain.Player;
import fi.oamk.ofkl.domain.TeamAssignment;
import org.apache.commons.lang.ObjectUtils;
import org.drools.planner.core.move.Move;
import org.drools.planner.core.score.director.ScoreDirector;

import java.util.Collection;
import java.util.Collections;

public class TeamAssignmentMove implements Move {

    private final Player player;
    private final TeamAssignment teamAssignment;

    public TeamAssignmentMove(TeamAssignment teamAssignment, Player player) {
        this.teamAssignment = teamAssignment;
        this.player = player;
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {

        return !ObjectUtils.equals(teamAssignment.getPlayer(), player);
    }

    @Override
    public Move createUndoMove(ScoreDirector scoreDirector) {
        return new TeamAssignmentMove(teamAssignment, teamAssignment.getPlayer());
    }

    @Override
    public void doMove(ScoreDirector scoreDirector) {
        TeamAssignmentMoveHelper.movePlayer(scoreDirector, teamAssignment, player);
    }

    @Override
    public Collection<TeamAssignment> getPlanningEntities() {
        return Collections.singletonList(teamAssignment);
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        return Collections.singletonList(player);
    }

    public String toString() {
         return player + " => " + teamAssignment;
     }

    public TeamAssignment getTeamAssignment() {
        return teamAssignment;
    }

    public Player getPlayer() {
        return player;
    }
}
