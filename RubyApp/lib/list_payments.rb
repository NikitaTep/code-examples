class ListPayments
  attr_accessor :list
  
  PAGE_SIZE = 4
  
  def initialize(list = [])
    @list= list
  end
  
  def << (payment)
    @list << payment
  end
  
  def amount_payments ()
    @list.size
  end
  
  def [](index)
    @list[index]
  end
  
  def page_maker(page)
    @list[page*PAGE_SIZE, PAGE_SIZE].map.with_index do |payment, index|
      {
        index: page*PAGE_SIZE + index,
        data: payment
      }
    end
  end
  
  def sort
    @list.sort! do |x, y|
      if x.name == y.name
        if x.surname == y.surname
          x.patron <=> y.patron
        else
          x.surname <=> y.surname
        end
      else
        x.name <=> y.name
      end
    end
  end
  
  def page_count
    @list.size/PAGE_SIZE + 1
  end
  
end