Embulk::JavaPlugin.register_parser(
  "jsonl", "org.embulk.parser.jsonline.JsonLineParserPlugin",
  File.expand_path('../../../../classpath', __FILE__))
