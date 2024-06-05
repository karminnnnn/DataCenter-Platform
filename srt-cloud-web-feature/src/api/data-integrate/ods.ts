import service from '@/utils/request'

export const useOdsApi = (id: number) => {
	return service.get('/data-integrate/ods/' + id)
}

export const useOdsSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/data-integrate/ods', dataForm)
	} else {
		return service.post('/data-integrate/ods', dataForm)
	}
}

export const getColumnInfoApi = (tableName: any) => {
	return service.get('/data-integrate/ods/' + tableName + '/column-info')
}

export const getTableDataApi = (tableName: any) => {
	return service.get('/data-integrate/ods/' + tableName + '/table-data')
}


// 以下为新增
// 数据表字段部分
export const useOdsColumnInfoApi = (fieldId: number) => {
	return service.get('/data-integrate/ods/column-info/' + fieldId)
}

export const useOdsColumnInfoSubmitApi = (dataForm: any) => {
	if (dataForm.fieldId) {
		return service.put('/data-integrate/ods/column-info', dataForm)
	} else {
		return service.post('/data-integrate/ods/column-info', dataForm)
	}
}

// 数据表数据部分
export const useOdsTableDataSubmitApi = (dataForm: any) => {
	if (dataForm.id) {
		return service.put('/data-integrate/ods/tabledata', dataForm)
	} else {
		return service.post('/data-integrate/ods/tabledata', dataForm)
	}
}