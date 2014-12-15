require 'matrix'
require 'ostruct'
require 'set'

require 'active_support/core_ext'

require 'rucola/base'
require 'rucola/corpus'
require 'rucola/document'
require 'rucola/extractors/chain'
require 'rucola/extractors/character'
require 'rucola/extractors/filter'
require 'rucola/extractors/mapper'
require 'rucola/extractors/replacer'
require 'rucola/extractors/remover'
require 'rucola/extractors/splitter'
require 'rucola/extractors/trimmer'
require 'rucola/metrics/dice_similarity'
require 'rucola/metrics/naive_cosine_similarity'
require 'rucola/metrics/size_difference'
require 'rucola/metrics/tf_idf_cosine_similarity'
require 'rucola/models/naive_vector_space'
require 'rucola/weighters/tf'
require 'rucola/weighters/idf'
require 'rucola/weighters/tf_idf'

module Rucola
end

