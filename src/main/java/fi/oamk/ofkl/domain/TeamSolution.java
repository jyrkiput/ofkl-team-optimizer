package fi.oamk.ofkl.domain;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TeamSolution implements Solution<HardAndSoftScore> {

    private List<Player> players;

    private List<TeamAssignment> teamAssignments = Lists.newArrayList();
    private HardAndSoftScore score;

    public TeamSolution(HardAndSoftScore score, List<TeamAssignment> teamAssignments, List<Player> players) {
        this.score = score;
        this.teamAssignments = teamAssignments;
        this.players = players;
    }

    public TeamSolution(List<Player> players, List<TeamAssignment> initialAssignment) {
        this.players = players;
        teamAssignments = initialAssignment;
    }

    @PlanningEntityCollectionProperty
    public List<TeamAssignment> getTeamAssignments() {
         return teamAssignments;
     }

    public List<Player> getPlayerList() {
             return players;
    }

    public HardAndSoftScore getScore() {
        return score;
    }

    public void setScore(HardAndSoftScore hardAndSoftScore) {
        score = hardAndSoftScore;
    }

    public Collection<?> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.addAll(players);
        return facts;
    }

    public Solution<HardAndSoftScore> cloneSolution() {
        List<TeamAssignment> clonedTeamAssignments = Lists.newArrayListWithCapacity(teamAssignments.size());
          for (TeamAssignment assignment : teamAssignments) {
              TeamAssignment clonedTeamAssignment = assignment.clone();
              clonedTeamAssignments.add(clonedTeamAssignment);
          }
        return new TeamSolution(score, clonedTeamAssignments, players);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamSolution)) {
            return false;
        } else {
            TeamSolution other = (TeamSolution) o;
            if (teamAssignments.size() != other.teamAssignments.size()) {
                return false;
            }
            for (Iterator<TeamAssignment> teamAssignment = teamAssignments.iterator(), otherIt = other.teamAssignments.iterator(); teamAssignment.hasNext();) {
                TeamAssignment assignment = teamAssignment.next();
                TeamAssignment otherAssignment = otherIt.next();
                if (!assignment.solutionEquals(otherAssignment)) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        for (TeamAssignment process : teamAssignments) {
            // Notice: we don't use hashCode()
            hashCodeBuilder.append(process.solutionHashCode());
        }
        return hashCodeBuilder.toHashCode();
    }
}
