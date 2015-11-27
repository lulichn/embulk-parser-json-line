package org.embulk.parser.jsonline;

import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.spi.Buffer;
import org.embulk.spi.GuessPlugin;

public class JsonLineParserGuessPlugin implements GuessPlugin {
    @Override
    public ConfigDiff guess(ConfigSource config, Buffer sample) {
        return null;
    }
}
