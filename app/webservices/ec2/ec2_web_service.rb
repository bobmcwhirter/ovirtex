import java.security.KeyPairGenerator
import org.ovirt.ec2.crypto.RSAKeyWriter

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

    generator = KeyPairGenerator.get_instance( "RSA" )
    generator.initialize__method( 4096 )

    key_pair = generator.genKeyPair

    writer = RSAKeyWriter.new

    private_material = writer.write_private_key( key_pair.get_private )
    public_material  = writer.write_public_key( key_pair.get_public )
    public_fingerprint = writer.write_public_key_fingerprint( key_pair.get_public )

    #ActiveRecord::Base.transaction do
    #  public_key = @user.public_keys.create( :name=>params[:name],
    #                                         :fingerprint=>public_fingerprint,
    #                                         :material=>public_material )
    #  flash[:private_key] = private_material
    #  redirect_to [ @user, public_key ]
    #end

    response = create_response
    response.requestId = "8675309"
    response.keyName = request.keyName
    response.keyFingerprint = public_fingerprint
    response.keyMaterial = private_material
    puts response.inspect
    response
  end

end
