require 'ovirt-ec2/base-handler'

class DescribeInstances < BaseHandler
  
  def handle
    reservationSet = factory.createReservationSetType
    
    for instance in request.getInstancesSet.getItem
      reservationInfo = factory.createReservationInfoType
      reservationInfo.setOwnerId( "bob" );
      reservationInfo.setReservationId( instance.getInstanceId.gsub( /i-/, 'r-' ) );
      reservationSet.item.add( reservationInfo )
    end
    
    response.setReservationSet(reservationSet);
    
  end
end