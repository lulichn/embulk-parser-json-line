Embulk::JavaPlugin.register_parser(
  "jsonl", "org.embulk.parser.jsonLine.JsonLineParserPlugin",
  File.expand_path('../../../../classpath', __FILE__))
