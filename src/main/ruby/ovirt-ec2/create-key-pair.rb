require 'ovirt-ec2/base-handler'

import java.security.KeyPairGenerator
import org.ovirt.ec2.crypto.RSAKeyWriter

class CreateKeyPair < BaseHandler
  
  def handle
    puts request.get_key_name
    
    keyGen = KeyPairGenerator::getInstance("RSA");
    keyGen.initialize__method( 2048 )
    key_pair = keyGen.gen_key_pair
    
    writer = RSAKeyWriter.new
    key_material = writer.write_private_key( key_pair.get_private );
    key_fingerprint = writer.write_public_key_fingerprint( key_pair.get_public );
    
    response.set_key_name request.get_key_name
    response.set_key_fingerprint key_fingerprint
    response.set_key_material key_material
  end
end