
class BaseHandler
  
  attr_accessor :factory
  
  attr_accessor :request
  attr_accessor :response
  
  def initialize
    @request  = nil 
    @response = nil
    @factory  = nil
  end
  
end