class User < ActiveRecord::Base

  has_many :email_addresses
  has_many :public_keys, :order=>:name

  def to_s
    self.display_name
  end

end
