import java.security.KeyPairGenerator
import org.ovirt.ec2.crypto.RSAKeyWriter

class PublicKeysController < ApplicationController

  before_filter :load_user
  before_filter :load_public_key, :only=>[ :show ]

  def index
    @public_keys = @user.public_keys
  end

  def new
    @public_key = @user.public_keys.build
  end

  def create
    generator = KeyPairGenerator.get_instance( "RSA" )
    generator.initialize__method( 4096 )

    key_pair = generator.genKeyPair

    writer = RSAKeyWriter.new
    private_material = writer.write_private_key( key_pair.get_private )
    public_material  = writer.write_public_key( key_pair.get_public )
    public_fingerprint = writer.write_public_key_fingerprint( key_pair.get_public )
 
    ActiveRecord::Base.transaction do
      public_key = @user.public_keys.create( :name=>params[:name], 
                                             :fingerprint=>public_fingerprint,
                                             :material=>public_material )
      flash[:private_key] = private_material
      redirect_to [ @user, public_key ]
    end

  end

  def show
  end

  private
 
  def load_user
    @user = User.find_by_id( params[:user_id] )
    redirect_to users_url and return false unless @user
  end

  def load_public_key
    @public_key = @user.public_keys.find_by_name( params[:id] )
    redirect_to user_public_keys_url( @user ) and return false unless @public_key
  end

end
