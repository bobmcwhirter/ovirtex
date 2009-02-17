class CreateCertificates < ActiveRecord::Migration
  def self.up
    create_table :certificates do |t|
      t.belongs_to :user
      t.string :dn,       :limit=>1024,  :null=>false
      t.string :material, :limit=>32768, :null=>false
      t.timestamps
    end
  end

  def self.down
    drop_table :certificates
  end
end
