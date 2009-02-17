class CreateEmailAddresses < ActiveRecord::Migration
  def self.up
    create_table :email_addresses do |t|
      t.belongs_to :user, :null=>true
      t.string  :address, :limit=>128, :null=>false, :unique=>true
      t.timestamps
    end
  end

  def self.down
    drop_table :email_addresses
  end
end
