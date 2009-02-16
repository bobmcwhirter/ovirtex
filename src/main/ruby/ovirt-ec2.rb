
import "com.amazonaws.ec2.doc._2008_12_01.ObjectFactory"

require 'ovirt-ec2/describe-instances'

require 'ovirt-ec2/create-key-pair'
require 'ovirt-ec2/describe-key-pairs'

class OVirtEC2
  
  def initialize()
    @factory = ObjectFactory.new    
  end
  
  def dispatch(operation, request)
    puts "dispatching #{operation} for #{request}" 
    handler_class = eval operation
    handler = handler_class.new
    handler.factory = @factory
    handler.request = request
    
    #response_class = eval "#{operation}ResponseType"
    response = @factory.send( "create#{operation}ResponseType" )
    response.setRequestId( "8675309decafbad")
    handler.response = response
    handler.handle
    return response
    #response = DescribeInstancesResponseType.new
    #response.setRequestId("d0763fa3c9eab58c15e5");
    #reservationSet = ReservationSetType.new
    #response.setReservationSet(reservationSet);
    #puts "returning response #{response}"
    #return response
  end
  
end

#OVirtEC2.new
