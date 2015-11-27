package org.embulk.parser.jsonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.embulk.config.Config;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;
import org.embulk.spi.*;
import org.embulk.spi.time.TimestampParser;
import org.embulk.spi.util.LineDecoder;
import org.embulk.spi.util.Timestamps;

import java.io.IOException;

public class JsonLineParserPlugin implements ParserPlugin {
    ObjectMapper mapper = new ObjectMapper();

    public interface PluginTask extends Task, LineDecoder.DecoderTask, TimestampParser.Task {
        @Config("schema")
        public SchemaConfig getSchema();
    }

    @Override
    public void transaction(ConfigSource config, ParserPlugin.Control control) {
        PluginTask task = config.loadConfig(PluginTask.class);
        control.run(task.dump(), task.getSchema().toSchema());
    }

    @Override
    public void run(TaskSource taskSource, Schema schema, FileInput input, PageOutput output) {
        // TimeStamp Schema
        PluginTask pluginTask = taskSource.loadTask(PluginTask.class);
        final TimestampParser[] timestampParsers = Timestamps.newTimestampColumnParsers(pluginTask, pluginTask.getSchema());

        // PageBuilder
        PageBuilder pageBuilder = new PageBuilder(Exec.getBufferAllocator(), schema, output);

        // Input
        LineDecoder.DecoderTask task = taskSource.loadTask(LineDecoder.DecoderTask.class);
        LineDecoder decoder = new LineDecoder(input, task);
        while (decoder.nextFile()) {
            while (true) {
                String line = decoder.poll();
                if (line == null) {
                    break;
                }

                try {
                    JsonNode nodes = mapper.readTree(line);
                    makeRecord(pageBuilder, schema, timestampParsers, nodes);
                } catch (IOException e) {
                    throw new DataException(e);
                }

                pageBuilder.addRecord();
            }
        }

        pageBuilder.finish();
    }

    private void makeRecord(final PageBuilder pageBuilder,
                            Schema schema,
                            final TimestampParser[] timestampParsers,
                            final JsonNode nodes) {
        schema.visitColumns(new ColumnVisitor() {
            @Override
            public void booleanColumn(Column column) {
                JsonNode node = nodes.get(column.getName());
                pageBuilder.setBoolean(column, node.booleanValue());
            }

            @Override
            public void longColumn(Column column) {
                JsonNode node = nodes.get(column.getName());
                pageBuilder.setLong(column, node.longValue());
            }

            @Override
            public void doubleColumn(Column column) {
                JsonNode node = nodes.get(column.getName());
                pageBuilder.setDouble(column, node.doubleValue());
            }

            @Override
            public void stringColumn(Column column) {
                JsonNode node = nodes.get(column.getName());
                pageBuilder.setString(column, node.textValue());
            }

            @Override
            public void timestampColumn(Column column) {
                TimestampParser parser = timestampParsers[column.getIndex()];
                JsonNode node = nodes.get(column.getName());
                pageBuilder.setTimestamp(column, parser.parse(node.textValue()));
            }
        });
    }
}
