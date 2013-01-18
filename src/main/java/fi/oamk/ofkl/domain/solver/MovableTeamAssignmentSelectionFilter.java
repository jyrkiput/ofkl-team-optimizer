package fi.oamk.ofkl.domain.solver;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fi.oamk.ofkl.app.OfklApp;
import fi.oamk.ofkl.domain.Player;
import fi.oamk.ofkl.domain.Team;
import fi.oamk.ofkl.domain.TeamAssignment;
import fi.oamk.ofkl.domain.TeamSolution;
import fi.oamk.ofkl.solver.move.TeamAssignmentMove;
import org.drools.planner.core.heuristic.selector.common.decorator.SelectionFilter;
import org.drools.planner.core.score.director.ScoreDirector;

import java.util.Map;
import java.util.Set;

public class MovableTeamAssignmentSelectionFilter implements SelectionFilter<TeamAssignmentMove> {

    @Override
    public boolean accept(ScoreDirector scoreDirector, TeamAssignmentMove selection) {
        TeamSolution workingSolution = (TeamSolution) scoreDirector.getWorkingSolution();
        return accept(workingSolution, selection);
    }

    public boolean accept(TeamSolution workingSolution, TeamAssignmentMove selection) {
        Set<Team> usedTeams = Sets.newHashSet();
        Map<Team.Level, Integer> playersOnLevel = Maps.newHashMap();
        Map<Team.Level, Integer> price = Maps.newHashMap();

        for(Team.Level level : Team.Level.values()) {
            playersOnLevel.put(level, 0);
            price.put(level, 0);
        }

        for(TeamAssignment t : workingSolution.getTeamAssignments()) {
            Player player = t.getPlayer();
            if(player.equals(selection.getPlayer()))// && !t.solutionEquals(selection.getTeamAssignment()))
                return false;
            usedTeams.add(player.getTeam());
            Team.Level level = player.getTeam().getLevel();
            Integer currentlyFromLevel = playersOnLevel.get(level);
            if(currentlyFromLevel >= OfklApp.PLAYERS_ON_LEVEL)
                return false;

            playersOnLevel.put(level, currentlyFromLevel + 1);
            Integer currentPrice = price.get(player.getTeam().getLevel());
            currentPrice += player.getPrice();
            price.put(player.getTeam().getLevel(), currentPrice);
        }

        Player playerToBeReplaced = selection.getTeamAssignment().getPlayer();
        Team previousTeam = playerToBeReplaced.getTeam();
        usedTeams.remove(previousTeam);
        Team newTeam = selection.getPlayer().getTeam();
        if(price.get(previousTeam.getLevel()) - playerToBeReplaced.getPrice() + selection.getPlayer().getPrice() > OfklApp.MAXIMUM_PRICE_ON_LEVEL) {
            return false;
        }

        return !usedTeams.contains(newTeam) && !previousTeam.getLevel().equals(newTeam.getLevel());

    }

}
