package com.narko.project.searchSpec;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.narko.project.payload.request.GetReqEntity;

/**
 * 
 * @author agus.wijanarko
 *
 * @param <T>
 * 
 * 
 *            search =>> format [nama_field][operand][value][:][logic] operand
 *            {: equal ~ like < less than > greater than} logic { or and ; =>>
 *            untuk akhiran} ex username:admin:; =>> cari by username = admin
 *            username~admin:and,status:ACTIVE:; =>> cari by username like admin
 *            dan status = ACTIVE
 * 
 *            sort =>> format [nama_field][:][order] operand { asc desc, def asc
 *            }
 *
 *            ex username:asc =>> sort by username asc username:asc,status:desc
 *            =>> sort by username asc dan status desc page =>> format
 *            [pageNo][:][pageSize]
 *
 *            ex 1:10 =>> halaman page ke 1 dengan 10 data tiap halaman
 */

@Service
public class MappingSpesification {

	public GetReqEntity toQuery(String search, String page, String sort) {

		String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
		String logicSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_LOGIC_SET);
		Pattern pattern1 = Pattern.compile("(\\w+?)(" + operationSetExper + ")(.+?)(:)(" + logicSetExper + "),");
		Pattern pattern3 = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\w+?),");
		Pattern pattern2 = Pattern.compile("(\\w+?)(:)(\\w+?),");

		GenericSpesification genericSpesification = null;
		Pageable pagination = null;
		List<Order> orders = new ArrayList<Order>();
		GetReqEntity getReq = null;

		try {
			if (sort != null && !sort.isEmpty() && !sort.contains(" ")) {
				Matcher matcher = pattern2.matcher(sort + ",");
				while (matcher.find()) {

					// cek param sort asc/desc
					if (matcher.group(3).toUpperCase().equals(Sort.Direction.ASC.name())
							|| matcher.group(3).toUpperCase().equals(Sort.Direction.DESC.name())) {

						Order order = new Order(Sort.Direction.valueOf(matcher.group(3).toUpperCase()),
								matcher.group(1));
						orders.add(order);
					}
					// cek param sort jika buka asc/desc, default asc
					else {
						Order order = new Order(Sort.Direction.ASC, matcher.group(1));
						orders.add(order);
					}

				}
			}

			// cek param sort null default sort by id asc
			else if (sort == null || sort.isEmpty() || sort.contains(" ")) {

				Order order = new Order(Sort.Direction.ASC, "id");
				orders.add(order);

			}

//			cek param search untuk fiter
//			jika null atau blank atau kosong skip
			if (search != null && !search.isEmpty()) {

				genericSpesification = new GenericSpesification<>();
				Matcher matcher = pattern1.matcher(search + ",");

				while (matcher.find()) {

					String valueStr = matcher.group(3);
					String logic = matcher.group(5);
					String operator = matcher.group(2);
					if (!logic.equals(";")) {
						logic = logic.toUpperCase();
					}

					if (valueStr.matches("\\d{4}-\\d{2}-\\d{2}")) {

						if (!operator.equals("~")) {
							genericSpesification.add(new SearchCriteria(matcher.group(1), valueStr,
									SearchOperation.getSimpleOperation(matcher.group(2)), logic, Date.class.getName()));
						}

					} else {

						genericSpesification.add(new SearchCriteria(matcher.group(1), valueStr,
								SearchOperation.getSimpleOperation(matcher.group(2)), logic, String.class.getName()));
					}

				}

			}

//			cek page search untuk pagination
//			jika null atau blank atau kosong skip			
			if (page != null && !page.isEmpty() && !page.contains(" ")) {
				String[] arrOfPage = page.split(":");

				int pageNo = Integer.valueOf(arrOfPage[0]) >= 0 ? Integer.valueOf(arrOfPage[0]) : 0;
				int pageSize = Integer.valueOf(arrOfPage[1]) > 0 ? Integer.valueOf(arrOfPage[1]) : 1;

				pagination = PageRequest.of(pageNo, pageSize, Sort.by(orders));

			}

			getReq = new GetReqEntity(genericSpesification, pagination, orders);

			return getReq;
		} catch (

		Exception e) {
			e.printStackTrace();
//			message = e.getMessage();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

			getReq = new GetReqEntity(null, null, null);

			return getReq;
		}

	}

}