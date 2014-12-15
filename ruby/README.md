# Wikipedia – Ruby

Parsing and processing of useful data from Wikipedia, DBPedia and Freebase utilizing Ruby.

## Libraries, examples and scripts

- [Rucola](example/rucola) - very simple information retrieval library for explanatory purposes.
- [Stopwatch](lib/stopwatch.rb) - simple stopwatch utility.

## Requirements

* Ruby 2.1

## Installation

Clone and install.

```
git clone git@github.com:irfiit/wikipedia.git
cd wikipedia/ruby
bundle install
```

## Contents

```
|-- example
|-- lib
|-- script
`-- spec
    `-- fixtures
```

- `example` Examples of custom library or script usage.
- `lib` Application specific libraries. Basically, any kind of custom code.
- `script` Helper scripts for automation, generation, etc.
- `spec` Unit and other tests along with fixtures. 

## Testing

Run specs with `bundle exec rspec`.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin new-feature`)
5. Create new Pull Request

## License

This software is released under the [Apache 2.0 License](../LICENSE).
