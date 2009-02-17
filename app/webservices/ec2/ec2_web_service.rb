
puts "loading EC2WebService"

load "#{RAILS_ROOT}/app/webservices/ec2/base_web_service.rb"

class Ec2WebService < BaseWebService

  target_namespace 'http://ec2.amazonaws.com/doc/2008-12-01/'
  port_name        'AmazonEC2'

  security do
    inbound do
      verify_signature
      verify_timestamp
    end
  end

  def describe_instances()
    puts "describe_instances #{request}"

    request.instancesSet.each{|item| puts item.instanceId}

    response = create_response
    response.requestId = "8675309"
    response
  end

  def describe_images()
    puts "describe_images #{request}"
   
    response = create_response

    response.requestId = "8675309"
    response
  end

  def create_key_pair()
    puts "create_key_pair #{request}"

    import 'org.ovirt.ec2.crypto.RSAKeyWriter'
    writer = RSAKeyWriter.new
    puts "WRITER --> #{writer}"
    response = create_response
    response.requestId = "8675309"
    response.keyName = request.keyName
    response.keyFingerprint = "fingerprintofhtekey"
    response.keyMaterial = "0xkeymaterial"
    response
  end

end
