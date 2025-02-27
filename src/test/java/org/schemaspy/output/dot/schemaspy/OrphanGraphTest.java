package org.schemaspy.output.dot.schemaspy;

import org.junit.jupiter.api.Test;
import org.schemaspy.SimpleRuntimeDotConfig;
import org.schemaspy.cli.CommandLineArgumentParser;
import org.schemaspy.cli.CommandLineArguments;
import org.schemaspy.model.Database;
import org.schemaspy.model.LogicalTable;
import org.schemaspy.model.Table;
import org.schemaspy.output.dot.DotConfig;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrphanGraphTest {

    @Test
    void orphanGraphContainsOnlyOrphan() {
        assertThat(
                new OrphanGraph(
                        new SimpleRuntimeDotConfig(
                                new TestFontConfig(),
                                parse("-rankdirbug"),
                                false,
                                false
                        ),
                        Arrays.asList(mockView(),realTable(), mockNotOrphan())
                ).dot()
        )
                .containsOnlyOnce("label=<");
    }


    private Table mockView() {
        Table table = mock(Table.class);
        when(table.isView()).thenReturn(true);
        return table;
    }

    private Table mockNotOrphan() {
        Table table = mock(Table.class);
        when(table.isOrphan(anyBoolean())).thenReturn(false);
        return table;
    }

    private Table realTable() {
        return new LogicalTable(mock(Database.class), "cat", "sch", "tab", "com");
    }
    private DotConfig parse(String... args) {
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
            .getDotConfig();
    }

}