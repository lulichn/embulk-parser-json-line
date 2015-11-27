Embulk::JavaPlugin.register_guess(
  "jsonl", "org.embulk.parser.jsonLine.JsonLineParserGuessPlugin",
  File.expand_path('../../../../classpath', __FILE__))
