require 'sinatra/base'
require 'sinatra/reloader'
require 'csv'
require_relative 'lib/list_payments'
require_relative 'lib/payment'

class PaymentApp < Sinatra::Base
  configure :development do
    register Sinatra::Reloader
  end
  
  set :repo, ListPayments.new()
  
  get '/' do
    if settings.repo.amount_payments == 0
      CSV.foreach("data.csv", {headers: true}) do |raw|
        settings.repo << Payment.new(raw['name'], raw['surname'], raw['patronymic'], raw['type'], raw['debts'], raw['amount_paid'])
      end
    end
    @amount = settings.repo.amount_payments
    erb :index
  end
  
  get '/list_payments' do
    page = 0
    if params['page']
      page = params['page'].to_i
    end
    @payments = settings.repo.page_maker(page)
    @page_count = settings.repo.page_count
    erb :list_payments
  end
  
  get '/list_payments/new' do
    erb :new_payment
  end
  
  get '/list_payments/:payment_id' do
    @id = params['payment_id'].to_i
    @page = params['payment_id'].to_i/4
    @payment = settings.repo[params['payment_id'].to_i]
    erb :payment
  end
  
  get '/find_by_name' do
    erb :find_by_name
  end
  
  get '/find_by_sum' do
    erb :find_by_sum
  end
  
  get '/show' do
    name = params['name']
    surname = params['surname']
    patron = params['patron']
    list = settings.repo.list.reject{|item| item.name != name || item.surname != surname || item.patron != patron}
    @messege = "Общая сумма задолжености: #{list.inject(0){|res, el| el.debts - el.amount_paid + res}}"
    @payments = ListPayments.new(list).page_maker(0)
    @page_count = 0
    erb :list_payments
  end
  
  get '/show_sum' do
    min = params['min'].to_i
    max = params['max'].to_i
    list = settings.repo.list.reject{|item| (item.debts - item.amount_paid) < min || (item.debts - item.amount_paid) > max}
    @payments = ListPayments.new(list).page_maker(0)
    @page_count = list.size/4 + 1
    erb :list_payments
  end
  
  post '/delete' do
    settings.repo.list.delete_at(params['payment_id'].to_i)
    redirect to("/list_payments")
  end
  
  post '/sort' do
    settings.repo.sort
    redirect to("/list_payments")
  end
  
  post '/list_payments' do
    settings.repo.list << Payment.new(params["name"], params["surname"], params["patron"], params['type'], params["debts"].to_i, params["amount_paid"].to_i)
    redirect to("/list_payments")
  end

  run! if app_file == $0
end
