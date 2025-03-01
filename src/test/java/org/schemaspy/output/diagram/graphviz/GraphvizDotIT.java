/*
 * Copyright (C) 2018 Nils Petzaell
 *
 * This file is part of SchemaSpy.
 *
 * SchemaSpy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SchemaSpy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SchemaSpy. If not, see <http://www.gnu.org/licenses/>.
 */
package org.schemaspy.output.diagram.graphviz;

import org.junit.Rule;
import org.junit.Test;
import org.schemaspy.cli.CommandLineArgumentParser;
import org.schemaspy.cli.CommandLineArguments;
import org.schemaspy.testing.ConfigRule;
import org.schemaspy.testing.Logger;
import org.schemaspy.testing.LoggingRule;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;

/**
 * @author Nils Petzaell
 */
public class GraphvizDotIT {

    @Rule
    public LoggingRule loggingRule = new LoggingRule();

    @Rule
    public ConfigRule configRule = new ConfigRule();

    @Test
    public void version2_26_0() {
        assumeThat(System.getProperty("os.name"), is("Linux"));
        GraphvizConfig graphvizConfig = parse("-gv", "src/test/resources/dotFakes/2.26.0");
        GraphvizDot graphvizDot = new GraphvizDot(graphvizConfig);
        assertThat(graphvizDot.isValid()).isTrue();
    }

    @Test
    public void version2_31_0() {
        assumeThat(System.getProperty("os.name"), is("Linux"));
        GraphvizConfig graphvizConfig = parse("-gv", "src/test/resources/dotFakes/2.31.0");
        GraphvizDot graphvizDot = new GraphvizDot(graphvizConfig);
        assertThat(graphvizDot.isValid()).isFalse();
    }

    @Test
    public void version2_32_0() {
        assumeThat(System.getProperty("os.name"), is("Linux"));
        GraphvizConfig graphvizConfig = parse("-gv", "src/test/resources/dotFakes/2.32.0");
        GraphvizDot graphvizDot = new GraphvizDot(graphvizConfig);
        assertThat(graphvizDot.isValid()).isTrue();
    }

    @Test
    @Logger(GraphvizDot.class)
    public void specifyRenderer() {
        assumeThat(System.getProperty("os.name"), is("Linux"));
        GraphvizConfig graphvizConfig = parse("-gv", "src/test/resources/dotFakes/2.32.0", "-renderer", ":gd");
        GraphvizDot graphvizDot = new GraphvizDot(graphvizConfig);
        assertThat(loggingRule.getLog()).contains("gd");
    }

    @Test
    @Logger(GraphvizDot.class)
    public void defaultRenderer() {
        assumeThat(System.getProperty("os.name"), is("Linux"));
        GraphvizConfig graphvizConfig = parse("-gv", "src/test/resources/dotFakes/2.32.0");
        GraphvizDot graphvizDot = new GraphvizDot(graphvizConfig);
        assertThat(loggingRule.getLog()).contains("cairo");
    }

    @Test
    @Logger(GraphvizDot.class)
    public void lowQualityRenderer() {
        assumeThat(System.getProperty("os.name"), is("Linux"));
        GraphvizConfig graphvizConfig = parse("-gv", "src/test/resources/dotFakes/2.32.0", "-lq");
        GraphvizDot graphvizDot = new GraphvizDot(graphvizConfig);
        assertThat(loggingRule.getLog()).contains("gd");
    }

    private GraphvizConfig parse(String... args) {
        String[] defaultArgs = {"-o", "out", "-sso"};
        return new CommandLineArgumentParser(
            new CommandLineArguments(),
            (option) -> null
        )
            .parse(
                Stream
                    .concat(
                        Arrays.stream(defaultArgs),
                        Arrays.stream(args)
                    ).toArray(String[]::new))
            .getGraphVizConfig();
    }

}