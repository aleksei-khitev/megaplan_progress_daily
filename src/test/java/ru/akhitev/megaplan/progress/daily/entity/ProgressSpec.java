package ru.akhitev.megaplan.progress.daily.entity;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProgressSpec {
    Progress progress;

    @Before
    public void setUp() {
        progress = new Progress();
        progress.setTookInWork(202);
        progress.setOurRefuges(37);
        progress.setAllCausesReserve(24);
        progress.setLaunchedInWork(100);
        progress.setCandidateRefuges(10);
    }

    @Test
    public void whenAllDataCorrectThenFormattedCalculateLaunchPortion() {
        assertThat(progress.getFormattedLaunchPortion())
                .as("Formatted launch protion have to be calculated correctly")
                .isEqualTo("70,92");
    }

    @Test
    public void whenAllDataCorrectThenFormattedRefugesLaunchPortion() {
        assertThat(progress.getFormattedRefugesPortion())
                .as("Formatted refuges protion have to be calculated correctly")
                .isEqualTo("4,95");
    }

    @Test
    public void whenAllDataCorrectThenFormattedQualityIndex() {
        assertThat(progress.getFormattedQualityIndex())
                .as("Formatted quality index have to be calculated correctly")
                .isEqualTo("14,33");
    }
}
