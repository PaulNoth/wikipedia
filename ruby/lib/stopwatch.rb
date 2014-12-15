class Stopwatch
  def initialize(ticker = Time)
    @ticker = ticker
  end

  def start(message, options = {})
    print "#{message || "Working"}#{options[:separator] || ' ... '}"
  
    @mark = @ticker.now
  end

  def stop(message = nil, options = {})
    @delta = @ticker.now - @mark

    message = message || options[:stop_message] || "done"
    conversion = options[:conversion] || lambda { |delta| " (#{delta.round 3}s)" }

    puts "#{message}#{conversion.call @delta}"
  end

  def measure(message, options = {})
    start message, options

    yield

    stop nil, options
  end
end

