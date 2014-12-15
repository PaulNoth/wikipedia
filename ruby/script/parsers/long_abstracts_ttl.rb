#!/usr/bin/env ruby

STDIN.each_line do |line|
  next if line =~ /\A\W?\#/

  data = line.strip.match(/\<([^\>]+)\>\s+\<([^\>]+)\>\s+\"(.+)\"\@sk\s+\./)

  STDOUT << "#{data[1]} #{data[3]}\n"
end

