package com.pmprogramms.expensetracker.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.pmprogramms.expensetracker.R
import com.pmprogramms.expensetracker.database.Database
import com.pmprogramms.expensetracker.enums.ExpenseType
import com.pmprogramms.expensetracker.helper.StringHelper
import com.pmprogramms.expensetracker.model.helper.ExpenseWithCategory
import com.pmprogramms.expensetracker.repository.ExpensesRepository
import com.pmprogramms.expensetracker.utils.DateHelper
import com.pmprogramms.expensetracker.utils.MathHelper

class AppWidgetProvider : AppWidgetProvider() {
    private lateinit var expensesRepository: ExpensesRepository
    private var views: RemoteViews? = null
    private var appWidgetManager: AppWidgetManager? = null
    private var ids: IntArray? = null

    private var expensesWithCategoryLiveDataDaily: LiveData<List<ExpenseWithCategory>>? = null
    private var expensesWithCategoryLiveDataWeekly: LiveData<List<ExpenseWithCategory>>? = null
    private var expensesWithCategoryLiveDataMonthly: LiveData<List<ExpenseWithCategory>>? = null

    private val observerDaily = object : Observer<List<ExpenseWithCategory>> {
        override fun onChanged(value: List<ExpenseWithCategory>) {
            ids?.forEach { id ->
                views?.setTextViewText(R.id.today_balance, MathHelper.sumExpenses(value).toString())
                appWidgetManager?.updateAppWidget(id, views)
            }
        }
    }

    private val observerWeekly = object : Observer<List<ExpenseWithCategory>> {
        override fun onChanged(value: List<ExpenseWithCategory>) {
            ids?.forEach { id ->
                views?.setTextViewText(R.id.weekly_balance, MathHelper.sumExpenses(value).toString())
                appWidgetManager?.updateAppWidget(id, views)
            }
        }
    }

    private val observerMonthly = object : Observer<List<ExpenseWithCategory>> {
        override fun onChanged(value: List<ExpenseWithCategory>) {
            ids?.forEach { id ->
                views?.setTextViewText(R.id.balance_text_view, MathHelper.sumExpenses(value).toString())
                appWidgetManager?.updateAppWidget(id, views)
            }
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds?.forEach { id ->
            context?.let {
                val expenseDao = Database.getDatabase(context).getExpensesDao()
                expensesRepository = ExpensesRepository(expenseDao)

                views =
                    RemoteViews(
                        context.packageName,
                        R.layout.monthly_overview_widget_layout
                    )

                this.ids = appWidgetIds
                this.appWidgetManager = appWidgetManager

                if (expensesWithCategoryLiveDataDaily == null) {
                    val dayRangeStart = DateHelper.dayRange().first
                    val dayRangeEnd = DateHelper.dayRange().second

                    expensesWithCategoryLiveDataDaily =
                        expensesRepository.getAllExpensesByRange(dayRangeStart, dayRangeEnd)
                    expensesWithCategoryLiveDataDaily?.observeForever(observerDaily)
                }

                if (expensesWithCategoryLiveDataWeekly == null) {
                    val weekRangeStart = DateHelper.weekRange().first
                    val weekRangeEnd = DateHelper.weekRange().second

                    expensesWithCategoryLiveDataWeekly =
                        expensesRepository.getAllExpensesByRange(weekRangeStart, weekRangeEnd)
                    expensesWithCategoryLiveDataWeekly?.observeForever(observerWeekly)
                }

                if (expensesWithCategoryLiveDataMonthly == null) {
                    val monthRangeStart = DateHelper.monthRange().first
                    val monthRangeEnd = DateHelper.monthRange().second

                    expensesWithCategoryLiveDataMonthly =
                        expensesRepository.getAllExpensesByRange(monthRangeStart, monthRangeEnd)
                    expensesWithCategoryLiveDataMonthly?.observeForever(observerMonthly)
                }
            }
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        expensesWithCategoryLiveDataDaily?.removeObserver(observerDaily)
        expensesWithCategoryLiveDataWeekly?.removeObserver(observerWeekly)
        expensesWithCategoryLiveDataMonthly?.removeObserver(observerMonthly)
    }

}