Embulk::JavaPlugin.register_guess(
  "jsonl", "org.embulk.parser.jsonline.JsonLineParserGuessPlugin",
  File.expand_path('../../../../classpath', __FILE__))
