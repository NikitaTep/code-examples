require 'simplecov'
SimpleCov.start
ENV['RACK_ENV'] = 'test'
require 'capybara'
require 'capybara/dsl'
require 'minitest/autorun'
require 'rack/test'

require_relative '../application'