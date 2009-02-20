
puts "loading BaseWebService"

require 'ostruct'

class BaseWebService

  attr_reader :request
  attr_reader :operation
  attr_reader :principal

  def self.target_namespace(ns=nil)
    ( @target_namspace = ns ) if ( ns != nil )
    @target_namspace ||= nil
  end

  def self.port_name(pn=nil)
    ( @port_name = pn ) if ( pn != nil )
    @port_name ||= nil
  end

  def self.security(&block) 
    unless block.nil?
      @security = Security.new( &block )
    end
    @security
  end

  class Security

    class InboundSecurity
      def initialize(&block)
        @verify_signature = false
        @verify_timestamp = false
        instance_eval &block
      end

      def verify_signature
        @verify_signature = true
      end

      def verify_timestamp
        @verify_timestamp = true
      end

      def verify_signature?
        @verify_signature
      end

      def verify_timestamp?
        @verify_timestamp
      end
    end

    class OutboundSecurity
      def initialize(&block)
        instance_eval &block
      end
    end

    def initialize(&block)
      @inbound  = nil
      @outbound = nil
      instance_eval &block
    end

    def inbound(&block)
      unless block.nil?
        @inbound = InboundSecurity.new( &block )
      end
      @inbound
    end

    def outbound(&block)
      unless block.nil?
        @outbound = OutboundSecurity.new(&block)
      end
    end
  end

  def self.setup_jboss_metadata(root)
    root.setTargetNamespace( self.target_namespace() )
    root.setPortName( self.port_name() )
    if ( @security )
      inbound = @security.inbound
      if ( inbound )
        root.get_inbound_security.setVerifySignature( inbound.verify_signature? )
        root.get_inbound_security.setVerifyTimestamp( inbound.verify_timestamp? )
        root.get_inbound_security.setTrustStore( "#{RAILS_ROOT}/auth/truststore.jks" )
      end
    end
  end

  def dispatch(principal, operation, request, response_creator=nil )
    @principal = principal
    @request   = request
    @operation = operation
    @response_creator = response_creator
    method_name = operation.underscore.to_sym
    puts "method [#{method_name}]"
    send( operation.to_s.underscore.to_sym )
  end

  def create_response
    if ( @response_creator ) 
      return eval @response_creator
    end
    nil
  end

end
