import java.security.KeyPairGenerator
import org.ovirt.ec2.crypto.RSAKeyWriter

class Ec2Endpoint < JBoss::Endpoints::BaseEndpoint

  target_namespace 'http://ec2.amazonaws.com/doc/2008-12-01/'
  port_name        'AmazonEC2'

  security do
    inbound do
      verify_timestamp
      verify_signature
    end
  end


  def create_key_pair()
    log.info( "request is #{request}" )

    generator = KeyPairGenerator.get_instance( "RSA" )
    generator.initialize__method( 2048 )

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
    log.debug  response.inspect 
    response
  end

end
